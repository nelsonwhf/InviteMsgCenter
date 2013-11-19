package com.duowan.gamebox.msgcenter.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import net.greghaines.jesque.Config;
import net.greghaines.jesque.ConfigBuilder;
import net.greghaines.jesque.client.Client;
import net.greghaines.jesque.client.ClientPoolImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

import com.duowan.common.loadbalance.FailOverProcessor;
import com.duowan.common.loadbalance.LoadBalancer;
import com.duowan.common.loadbalance.Processor;
import com.duowan.common.loadbalance.RoundRobinLoadBalancer;
import com.duowan.gamebox.msgcenter.manager.ServiceManager;
import com.duowan.gamebox.msgcenter.manager.TeamManager;
import com.duowan.gamebox.msgcenter.manager.YyTokenManager;
import com.duowan.gamebox.msgcenter.manager.impl.InvitationManagerImpl;
import com.duowan.gamebox.msgcenter.manager.impl.NotifyManagerImpl;
import com.duowan.gamebox.msgcenter.manager.impl.ServiceManagerImpl;
import com.duowan.gamebox.msgcenter.manager.impl.TeamManagerImpl;
import com.duowan.gamebox.msgcenter.manager.impl.UserCryptManagerImpl;
import com.duowan.gamebox.msgcenter.manager.impl.UserManagerImpl;
import com.duowan.gamebox.msgcenter.manager.impl.VerifyAppTokenProcessor;
import com.duowan.gamebox.msgcenter.manager.impl.YyTokenManagerImpl;
import com.duowan.gamebox.msgcenter.manager.vo.RateLimitation;
import com.duowan.gamebox.msgcenter.plugin.PreSendInvitationPlugin;
import com.duowan.gamebox.msgcenter.plugin.impl.CheckUserInChannelPluginImpl;
import com.duowan.gamebox.msgcenter.plugin.impl.GetChannelShortIdProcessor;
import com.duowan.gamebox.msgcenter.plugin.impl.LimitRateSendInvitationPluginImpl;
import com.duowan.gamebox.msgcenter.rate.RateLimitManager;
import com.duowan.gamebox.msgcenter.rate.impl.RateLimitManagerImpl;
import com.duowan.gamebox.msgcenter.redis.NameSpace;
import com.duowan.gamebox.msgcenter.redis.RedisConfig;
import com.duowan.gamebox.msgcenter.redis.RedisPoolFactory;
import com.duowan.gamebox.msgcenter.serde.JsonObjectMapper;
import com.duowan.gamebox.msgcenter.serde.MessageDeserializerFactory;
import com.duowan.gamebox.msgcenter.serde.MessageSerializer;
import com.duowan.gamebox.msgcenter.serialize.impl.BytesSerializeClientRedisQueueImpl;
import com.duowan.gamebox.msgcenter.service.call.ServiceManagerCaller;
import com.duowan.gamebox.msgcenter.service.call.ServiceManagerCallerFactory;
import com.duowan.logcenter.client.impl.BytesLogClientRedisQueueImpl;
import com.duowan.udb.service.VerifyAppTokenReq;
import com.duowan.udb.service.VerifyAppTokenRes;

@WebListener(value = "MsgCenterListener")
public class MsgCenterListener implements ServletContextListener {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void contextInitialized(ServletContextEvent event) {
		log.info("Start MsgCenterListener contextInitialized()");

		SingletonVars.get().setJsonObjectMapper(JsonObjectMapper.getObjectMapper());
		SingletonVars.get().setMessageSerializer(new MessageSerializer());
		SingletonVars.get().setRequestMessageDeserializer(
				MessageDeserializerFactory.get().getRequestMessageDeserializer());
		SingletonVars.get().setResponseMessageDeserializer(
				MessageDeserializerFactory.get().getResponseMessageDeserializer());

		ServletContext context = event.getServletContext();

		String notifyQueue = context.getInitParameter("notifyQueue");
		log.info("notifyQueue: " + notifyQueue);
		if (notifyQueue == null) {
			throw new RuntimeException("Must set notifuQueue");
		}

		RedisConfig redisConfig = new RedisConfig(context);
		log.info(redisConfig.toString());
		SingletonVars.get().setRedisConfig(redisConfig);

		Pool<Jedis> jedisPool = RedisPoolFactory.get(redisConfig).getJedisPool();
		SingletonVars.get().setJedisPool(jedisPool);

		Config jesqueConfig = new ConfigBuilder().withHost(redisConfig.getRedisHost())
				.withPort(redisConfig.getRedisPort()).withDatabase(redisConfig.getRedisDb())
				.withPassword(redisConfig.getRedisPassword())
				.withTimeout(redisConfig.getRedisTimeout()).withNamespace(NameSpace.NOTIFY_QUEUE)
				.build();
		SingletonVars.get().setJesqueConfig(jesqueConfig);
		Client queueClient = new ClientPoolImpl(jesqueConfig, jedisPool);
		SingletonVars.get().setQueueClient(queueClient);

		String logQueque = context.getInitParameter("logQueue");
		if (logQueque == null)
			logQueque = "Q01";
		BytesLogClientRedisQueueImpl logClient = new BytesLogClientRedisQueueImpl();
		logClient.setLogQueueName(logQueque);
		logClient.setQueueClient(queueClient);

		BytesSerializeClientRedisQueueImpl serializeClient = new BytesSerializeClientRedisQueueImpl();
		serializeClient.setLogClient(logClient);

		UserCryptManagerImpl userCryptManager = new UserCryptManagerImpl();
		userCryptManager.setJedisPool(jedisPool);
		userCryptManager.initCryptKeys();

		UserManagerImpl userManager = new UserManagerImpl();
		userManager.setJedisPool(jedisPool);
		userManager.setJsonObjectMapper(JsonObjectMapper.getObjectMapper());

		NotifyManagerImpl notifyManager = new NotifyManagerImpl();
		notifyManager.setJesqueConfig(jesqueConfig);
		notifyManager.setNotifyQueue(notifyQueue);

		TeamManagerImpl teamManager = new TeamManagerImpl();
		teamManager.setJedisPool(jedisPool);
		String teamTimeoutStr = context.getInitParameter("teamTimeout");
		int teamTimeout = (teamTimeoutStr == null) ? TeamManager.DEFAULT_TIMEOUT : Integer
				.parseInt(teamTimeoutStr);
		teamManager.setTimeOut(teamTimeout);

		YyTokenManager yyTokenManager = buildYyTokenManager(context);

		InitialContext cxt = null;
		DataSource ds = null;
		try {
			cxt = new InitialContext();
			ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/gamebox_msgcener");
		} catch (NamingException e) {
			log.error("Find datasource jdbc/gamebox_msgcener failed: " + e, e);
		}

		InvitationManagerImpl invitationManager = new InvitationManagerImpl();
		invitationManager.setDataSource(ds);
		invitationManager.setJedisPool(jedisPool);
		invitationManager.setJsonObjectMapper(JsonObjectMapper.getObjectMapper());

		CheckUserInChannelPluginImpl checkChannelPlugin = new CheckUserInChannelPluginImpl();
		checkChannelPlugin.setDataSource(ds);
		checkChannelPlugin.setJedisPool(jedisPool);
		FailOverProcessor<String, String> getChShortIdProcessor = buildGetChShortIdFailOverProcessor(context);
		if (getChShortIdProcessor == null) {
			checkChannelPlugin.setDebugMode(true);
			log.info("checkChannelPlugin debug mode: true");
		} else {
			checkChannelPlugin.setFailOverProcessor(getChShortIdProcessor);
			log.info("checkChannelPlugin failOverProcessor: " + getChShortIdProcessor);
		}

		RateLimitManager inviteLimitManager = buildInviteRateLimitManager(context, jedisPool);
		LimitRateSendInvitationPluginImpl limitPlugin = new LimitRateSendInvitationPluginImpl();
		limitPlugin.setInviteLimitManager(inviteLimitManager);

		List<PreSendInvitationPlugin> preSendPlugins = new ArrayList<PreSendInvitationPlugin>();
		preSendPlugins.add(checkChannelPlugin);
		preSendPlugins.add(limitPlugin);

		ServiceManagerImpl serviceManager = new ServiceManagerImpl();
		serviceManager.setJedisPool(jedisPool);
		serviceManager.setQueueClient(queueClient);
		serviceManager.setUserCryptManager(userCryptManager);
		serviceManager.setUserManager(userManager);
		serviceManager.setNotifyManager(notifyManager);
		serviceManager.setTeamManager(teamManager);
		serviceManager.setYyTokenManager(yyTokenManager);
		serviceManager.setInvitationManager(invitationManager);
		serviceManager.setPreSendInvitationPlugins(preSendPlugins);
		serviceManager.setSerializeClient(serializeClient);

		SingletonVars.get().setServiceManager(serviceManager);

		ServiceManagerCaller serviceManagerCaller = ServiceManagerCallerFactory.get()
				.getServiceManagerCaller();
		SingletonVars.get().setServiceManagerCaller(serviceManagerCaller);

		// register myself
		String serverIpAndPort = context.getInitParameter("serverIpAndPort");
		if (serverIpAndPort == null) {
			throw new RuntimeException("Must set serverIpAndPort");
		}
		String serverUrl = new StringBuilder().append("http://").append(serverIpAndPort)
				.append(context.getContextPath()).toString();
		log.info("This server URL is " + serverUrl);
		SingletonVars.get().setServerUrl(serverUrl);
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.sadd(NameSpace.SERVERS_KEY, serverUrl);
		} finally {
			jedisPool.returnResource(jedis);
		}
		serviceManager.startService();
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		Pool<Jedis> jedisPool = SingletonVars.get().getJedisPool();
		if (jedisPool != null && SingletonVars.get().getServerUrl() != null) {
			Jedis jedis = jedisPool.getResource();
			try {
				jedis.srem(NameSpace.SERVERS_KEY, SingletonVars.get().getServerUrl());
			} finally {
				jedisPool.returnResource(jedis);
			}
		}

		ServiceManager serviceManager = SingletonVars.get().getServiceManager();
		if (serviceManager != null) {
			serviceManager.stopService();
		}

		if (jedisPool != null) {
			jedisPool.destroy();
		}
	}

	private YyTokenManager buildYyTokenManager(ServletContext context) {
		YyTokenManagerImpl yyTokenManager = new YyTokenManagerImpl();
		String udbServerHost = context.getInitParameter("udbServerHost");
		String udbServerPortStr = context.getInitParameter("udbServerPort");
		if (udbServerHost == null || udbServerPortStr == null) {
			yyTokenManager.setDebugMode(true);
			log.info("yyTokenManager debug mode: true");
		} else {
			LoadBalancer<Processor<VerifyAppTokenReq, VerifyAppTokenRes>> loadBalancer = new RoundRobinLoadBalancer<Processor<VerifyAppTokenReq, VerifyAppTokenRes>>();
			Pattern pattern = Pattern.compile(",");
			String[] arrHosts = pattern.split(udbServerHost);
			String[] arrPorts = pattern.split(udbServerPortStr);
			if (arrHosts.length == arrPorts.length) {
				for (int i = 0; i < arrHosts.length; i++) {
					VerifyAppTokenProcessor processor = new VerifyAppTokenProcessor();
					processor.setHost(arrHosts[i]);
					processor.setPort(Integer.parseInt(arrPorts[i]));
					loadBalancer.addProcessor(processor);
				}
				FailOverProcessor<VerifyAppTokenReq, VerifyAppTokenRes> failOverProcessor = new FailOverProcessor<VerifyAppTokenReq, VerifyAppTokenRes>();
				failOverProcessor.setLoadBalancer(loadBalancer);

				yyTokenManager.setDebugMode(false);
				yyTokenManager.setFailOverProcessor(failOverProcessor);
				log.info("YyTokenManager failOverProcessor: " + failOverProcessor);
			} else {
				log.error("udbServerHost splitted length != udbServerPort splitted length");
				log.info("yyTokenManager debug mode: true");
				yyTokenManager.setDebugMode(true);
			}
		}
		return yyTokenManager;
	}

	private RateLimitManager buildInviteRateLimitManager(ServletContext context,
			Pool<Jedis> jedisPool) {
		RateLimitManagerImpl inviteLimitManager = new RateLimitManagerImpl();
		inviteLimitManager.setJedisPool(jedisPool);
		RateLimitation inviteLimit = RateLimitation.parseRateLimitation(context
				.getInitParameter("inviteLimit"));
		inviteLimitManager.setRateLimitation(inviteLimit);
		if (inviteLimit == null) {
			log.info("Invite Limit is UNLIMIT");
		} else {
			log.info("Invite Limit is " + inviteLimit);
		}
		return inviteLimitManager;
	}

	private FailOverProcessor<String, String> buildGetChShortIdFailOverProcessor(
			ServletContext context) {
		String webdbServerHost = context.getInitParameter("webdbServerHost");
		String webdbServerPort = context.getInitParameter("webdbServerPort");
		if (webdbServerHost == null || webdbServerPort == null) {
			return null;
		} else {
			LoadBalancer<Processor<String, String>> loadBalancer = new RoundRobinLoadBalancer<Processor<String, String>>();
			Pattern pattern = Pattern.compile(",");
			String[] arrHosts = pattern.split(webdbServerHost);
			String[] arrPorts = pattern.split(webdbServerPort);
			if (arrHosts.length == arrPorts.length) {
				for (int i = 0; i < arrHosts.length; i++) {
					GetChannelShortIdProcessor processor = new GetChannelShortIdProcessor();
					processor.setHost(arrHosts[i]);
					processor.setPort(Integer.parseInt(arrPorts[i]));
					loadBalancer.addProcessor(processor);
				}
				FailOverProcessor<String, String> failOverProcessor = new FailOverProcessor<String, String>();
				failOverProcessor.setLoadBalancer(loadBalancer);
				return failOverProcessor;
			} else {
				log.error("webdbServerHost splitted length != webdbServerPort splitted length");
				return null;
			}
		}
	}

}
