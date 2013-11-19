package com.duowan.gamebox.msgcenter.listener;

import java.util.Random;

import net.greghaines.jesque.Config;
import net.greghaines.jesque.client.Client;

import org.codehaus.jackson.map.ObjectMapper;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

import com.duowan.gamebox.msgcenter.manager.ServiceManager;
import com.duowan.gamebox.msgcenter.redis.RedisConfig;
import com.duowan.gamebox.msgcenter.serde.MessageDeserializer;
import com.duowan.gamebox.msgcenter.serde.MessageSerializer;
import com.duowan.gamebox.msgcenter.service.call.ServiceManagerCaller;

public class SingletonVars {

	private MessageDeserializer requestMessageDeserializer;
	private MessageDeserializer responseMessageDeserializer;
	private MessageSerializer messageSerializer;
	private Pool<Jedis> jedisPool;
	private RedisConfig redisConfig;
	private Config jesqueConfig;
	private Client queueClient;
	private ObjectMapper jsonObjectMapper;

	private ServiceManager serviceManager;
	private ServiceManagerCaller serviceManagerCaller;

	private String serverUrl;

	private Random random = new Random(System.currentTimeMillis());

	private SingletonVars() {
	}

	/**
	 * @return the requestMessageDeserializer
	 */
	public MessageDeserializer getRequestMessageDeserializer() {
		return requestMessageDeserializer;
	}

	/**
	 * @param requestMessageDeserializer
	 *            the requestMessageDeserializer to set
	 */
	public void setRequestMessageDeserializer(MessageDeserializer requestMessageDeserializer) {
		this.requestMessageDeserializer = requestMessageDeserializer;
	}

	/**
	 * @return the responseMessageDeserializer
	 */
	public MessageDeserializer getResponseMessageDeserializer() {
		return responseMessageDeserializer;
	}

	/**
	 * @param responseMessageDeserializer
	 *            the responseMessageDeserializer to set
	 */
	public void setResponseMessageDeserializer(MessageDeserializer responseMessageDeserializer) {
		this.responseMessageDeserializer = responseMessageDeserializer;
	}

	/**
	 * @return the jedisPool
	 */
	public Pool<Jedis> getJedisPool() {
		return jedisPool;
	}

	/**
	 * @param jedisPool
	 *            the jedisPool to set
	 */
	public void setJedisPool(Pool<Jedis> jedisPool) {
		this.jedisPool = jedisPool;
	}

	/**
	 * @return the redisConfig
	 */
	public RedisConfig getRedisConfig() {
		return redisConfig;
	}

	/**
	 * @param redisConfig
	 *            the redisConfig to set
	 */
	public void setRedisConfig(RedisConfig redisConfig) {
		this.redisConfig = redisConfig;
	}

	/**
	 * @return the jesqueConfig
	 */
	public Config getJesqueConfig() {
		return jesqueConfig;
	}

	/**
	 * @param jesqueConfig
	 *            the jesqueConfig to set
	 */
	public void setJesqueConfig(Config jesqueConfig) {
		this.jesqueConfig = jesqueConfig;
	}

	/**
	 * @return the queueClient
	 */
	public Client getQueueClient() {
		return queueClient;
	}

	/**
	 * @param queueClient
	 *            the queueClient to set
	 */
	public void setQueueClient(Client queueClient) {
		this.queueClient = queueClient;
	}

	/**
	 * @return the serviceManager
	 */
	public ServiceManager getServiceManager() {
		return serviceManager;
	}

	/**
	 * @param serviceManager
	 *            the serviceManager to set
	 */
	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}

	/**
	 * @return the jsonObjectMapper
	 */
	public ObjectMapper getJsonObjectMapper() {
		return jsonObjectMapper;
	}

	/**
	 * @param jsonObjectMapper
	 *            the jsonObjectMapper to set
	 */
	public void setJsonObjectMapper(ObjectMapper jsonObjectMapper) {
		this.jsonObjectMapper = jsonObjectMapper;
	}

	/**
	 * @return the messageSerializer
	 */
	public MessageSerializer getMessageSerializer() {
		return messageSerializer;
	}

	/**
	 * @param messageSerializer
	 *            the messageSerializer to set
	 */
	public void setMessageSerializer(MessageSerializer messageSerializer) {
		this.messageSerializer = messageSerializer;
	}

	/**
	 * @return the serviceManagerCaller
	 */
	public ServiceManagerCaller getServiceManagerCaller() {
		return serviceManagerCaller;
	}

	/**
	 * @param serviceManagerCaller
	 *            the serviceManagerCaller to set
	 */
	public void setServiceManagerCaller(ServiceManagerCaller serviceManagerCaller) {
		this.serviceManagerCaller = serviceManagerCaller;
	}

	/**
	 * @return the serverUrl
	 */
	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * @param serverUrl
	 *            the serverUrl to set
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	/**
	 * @return the random
	 */
	public Random getRandom() {
		return random;
	}

	private static final SingletonVars singletonVars = new SingletonVars();

	public static final SingletonVars get() {
		return singletonVars;
	}

}
