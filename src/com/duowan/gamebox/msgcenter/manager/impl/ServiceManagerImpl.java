package com.duowan.gamebox.msgcenter.manager.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.AsyncContext;

import net.greghaines.jesque.Job;
import net.greghaines.jesque.client.Client;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

import com.duowan.gamebox.msgcenter.listener.SingletonVars;
import com.duowan.gamebox.msgcenter.manager.InvitationManager;
import com.duowan.gamebox.msgcenter.manager.NotifyManager;
import com.duowan.gamebox.msgcenter.manager.ServiceManager;
import com.duowan.gamebox.msgcenter.manager.TeamManager;
import com.duowan.gamebox.msgcenter.manager.UserCryptManager;
import com.duowan.gamebox.msgcenter.manager.UserManager;
import com.duowan.gamebox.msgcenter.manager.YyTokenManager;
import com.duowan.gamebox.msgcenter.manager.exception.AuthorizeErrorException;
import com.duowan.gamebox.msgcenter.manager.exception.InvalidRequestException;
import com.duowan.gamebox.msgcenter.manager.exception.InvalidYyTokenException;
import com.duowan.gamebox.msgcenter.manager.exception.ServiceErrorException;
import com.duowan.gamebox.msgcenter.manager.vo.Invitation;
import com.duowan.gamebox.msgcenter.manager.vo.InvitationDetail;
import com.duowan.gamebox.msgcenter.manager.vo.User;
import com.duowan.gamebox.msgcenter.msg.AbstractMessage;
import com.duowan.gamebox.msgcenter.msg.request.JoinGameRequest;
import com.duowan.gamebox.msgcenter.msg.request.LoginRequest;
import com.duowan.gamebox.msgcenter.msg.request.QueryInvitationsRequest;
import com.duowan.gamebox.msgcenter.msg.request.QueryPartnersRequest;
import com.duowan.gamebox.msgcenter.msg.request.SendInvitationRequest;
import com.duowan.gamebox.msgcenter.msg.request.SendReplyRequest;
import com.duowan.gamebox.msgcenter.msg.response.JoinGameResponse;
import com.duowan.gamebox.msgcenter.msg.response.LoginResponse;
import com.duowan.gamebox.msgcenter.msg.response.NoopResponse;
import com.duowan.gamebox.msgcenter.msg.response.NotifyInvitationResponse;
import com.duowan.gamebox.msgcenter.msg.response.NotifyReplyResponse;
import com.duowan.gamebox.msgcenter.msg.response.QueryInvitationsResponse;
import com.duowan.gamebox.msgcenter.msg.response.QueryPartnersResponse;
import com.duowan.gamebox.msgcenter.msg.response.SendInvitationResponse;
import com.duowan.gamebox.msgcenter.msg.response.SendReplyResponse;
import com.duowan.gamebox.msgcenter.msg.vo.CommonRequestMsg;
import com.duowan.gamebox.msgcenter.msg.vo.LoginRequestMsg;
import com.duowan.gamebox.msgcenter.msg.vo.LoginResponseMsg;
import com.duowan.gamebox.msgcenter.msg.vo.NotifyInvitationMsg;
import com.duowan.gamebox.msgcenter.msg.vo.NotifyReplyMsg;
import com.duowan.gamebox.msgcenter.msg.vo.QueryPartnersRsponseMsg;
import com.duowan.gamebox.msgcenter.msg.vo.SendInvitationRequestMsg;
import com.duowan.gamebox.msgcenter.msg.vo.SendInvitationResponseMsg;
import com.duowan.gamebox.msgcenter.msg.vo.SendReplyRequestMsg;
import com.duowan.gamebox.msgcenter.msg.vo.SendReplyResponseMsg;
import com.duowan.gamebox.msgcenter.plugin.PreSendInvitationPlugin;
import com.duowan.gamebox.msgcenter.queue.NotifyCloseConnection;
import com.duowan.gamebox.msgcenter.queue.NotifyInvitation;
import com.duowan.gamebox.msgcenter.queue.NotifyInvitationMsgInternal;
import com.duowan.gamebox.msgcenter.queue.NotifyJobTypes;
import com.duowan.gamebox.msgcenter.queue.NotifyJoinGame;
import com.duowan.gamebox.msgcenter.queue.NotifyReply;
import com.duowan.gamebox.msgcenter.queue.NotifyReplyMsgInternal;
import com.duowan.gamebox.msgcenter.serde.MessageSerializer;
import com.duowan.gamebox.msgcenter.serialize.impl.BytesSerializeClientRedisQueueImpl;

public class ServiceManagerImpl implements ServiceManager {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final NoopResponse noopResponse = new NoopResponse();

	private AtomicBoolean isRunning = new AtomicBoolean(true);

	private Pool<Jedis> jedisPool;
	private Client queueClient;
	private ConcurrentMap<String, AsyncContext> mapTokenAsyncContext = new ConcurrentHashMap<String, AsyncContext>();
	private ConcurrentMap<AsyncContext, String> mapAsyncContextToken = new ConcurrentHashMap<AsyncContext, String>();

	private MessageSerializer messageSerializer = SingletonVars.get().getMessageSerializer();
	private ObjectMapper objectMapper = SingletonVars.get().getJsonObjectMapper();

	private DelayQueue<DelayedUserToken> noopDelayQueue = new DelayQueue<DelayedUserToken>();
	private int noopDelaySeconds = 60;
	private Thread noopThread;
	private NoopRunner noopRunner;

	private UserCryptManager userCryptManager;
	private UserManager userManager;
	private NotifyManager notifyManager;
	private TeamManager teamManager;
	private YyTokenManager yyTokenManager;
	private InvitationManager invitationManager;

	private BytesSerializeClientRedisQueueImpl serializeClient;

	private List<PreSendInvitationPlugin> preSendInvitationPlugins;

	private int secondsIn = 24 * 3600; // 24 hours

	private void addTokenAsyncContext(String token, AsyncContext asyncContext) {
		mapTokenAsyncContext.put(token, asyncContext);
		mapAsyncContextToken.put(asyncContext, token);
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.ServiceManager#login(com.duowan.gamebox.msgcenter.msg.request.LoginRequest, javax.servlet.AsyncContext)
	 */
	@Override
	public LoginResponse login(LoginRequest request, AsyncContext asyncContext)
			throws InvalidRequestException, ServiceErrorException, AuthorizeErrorException {
		checkRunningStatus();
		if (log.isInfoEnabled()) {
			log.info("Invoke login() with request: " + request + " with " + asyncContext);
		}
		checkRequestMsg(request);
		LoginRequestMsg loginRequest = request.getMsg();
		if (loginRequest.getGameType() == null) {
			throw new InvalidRequestException("Type is null");
		}
		if (loginRequest.getGameUidEncrypt() == null) {
			throw new InvalidRequestException("Uid encrypt is null");
		}
		String userId = null;
		try {
			userId = userCryptManager.decryptUserId(loginRequest);
		} catch (GeneralSecurityException e) {
			throw new AuthorizeErrorException("Authorize Error: " + e, e);
		}

		User user = userManager.getUserByIdAndType(userId, loginRequest.getGameType());
		if (user != null) {
			if (log.isInfoEnabled()) {
				log.info("Find prev user, need close prev connection: " + user);
			}
			String oldToken = user.getToken();
			String oldAtQueue = user.getAtQueue();
			Job job = new Job(NotifyJobTypes.NotifyCloseConnection, new Object[] { oldToken });
			queueClient.enqueue(oldAtQueue, job);
		}

		user = new User();
		user.setUserId(userId);
		user.setUserType(loginRequest.getGameType());
		user.setUserIdEncrypt(loginRequest.getGameUidEncrypt());
		user.setDisplayName(loginRequest.getDisplayName());
		user.setAtQueue(notifyManager.getNotifyQueue());
		userManager.generateToken(user);
		userManager.addUser(user);
		if (log.isInfoEnabled()) {
			log.info("User logined: " + user + " with " + asyncContext);
		}

		String token = user.getToken();
		addTokenAsyncContext(token, asyncContext);
		noopDelayQueue.put(new DelayedUserToken(token, noopDelaySeconds));

		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setMsg(new LoginResponseMsg(token, user));
		return loginResponse;
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.ServiceManager#joinGame(com.duowan.gamebox.msgcenter.msg.request.JoinGameRequest)
	 */
	@Override
	public JoinGameResponse joinGame(JoinGameRequest request) throws InvalidRequestException,
			ServiceErrorException {
		checkRunningStatus();
		checkRequestMsg(request);
		String token = request.getMsg().getToken();
		String atTeam = request.getMsg().getSummary();
		User user = getAndCheckUserByToken(token);
		if (user.getAtTeam() != null) {
			String prevTeam = user.getAtTeam();
			teamManager.removeTokenFromTeam(token, prevTeam);
			if (!atTeam.equals(prevTeam)) {
				notifyMemebersOfTeam(prevTeam);
			}
		}
		teamManager.addTokenToTeam(token, atTeam);
		user.setAtTeam(atTeam);
		userManager.updateUser(user);

		// notify all members of the team
		notifyMemebersOfTeam(atTeam);
		return new JoinGameResponse();
	}

	private void notifyMemebersOfTeam(String atTeam) {
		Set<String> members = teamManager.getTokens(atTeam);
		if (members != null && members.size() != 0) {
			for (String member : members) {
				User userTo = userManager.getUserByToken(member);
				if (userTo != null) {
					Job job = new Job(NotifyJobTypes.NotifyJoinGame,
							new Object[] { member, atTeam });
					queueClient.enqueue(userTo.getAtQueue(), job);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.ServiceManager#queryPartners(com.duowan.gamebox.msgcenter.msg.request.QueryPartnersRequest)
	 */
	@Override
	public QueryPartnersResponse queryPartners(QueryPartnersRequest request)
			throws InvalidRequestException, ServiceErrorException {
		checkRunningStatus();
		checkRequestMsg(request);
		String token = request.getMsg().getToken();
		String atTeam = request.getMsg().getSummary();
		User userFrom = getAndCheckUserByToken(token);
		if (!atTeam.equals(userFrom.getAtTeam())) {
			throw new InvalidRequestException("User not in team: " + atTeam);
		}
		return genQueryPartnersResponse(token, atTeam);
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.ServiceManager#sendInvitation(com.duowan.gamebox.msgcenter.msg.request.SendInvitationRequest)
	 */
	@Override
	public SendInvitationResponse sendInvitation(SendInvitationRequest request)
			throws InvalidRequestException, ServiceErrorException {
		checkRunningStatus();
		checkRequestMsg(request);
		SendInvitationRequestMsg reqMsg = request.getMsg();
		if (reqMsg.getDatas() == null || reqMsg.getDatas().length() == 0) {
			throw new InvalidRequestException("Invalid datas: null");
		}
		String token = reqMsg.getToken();
		String atTeam = reqMsg.getSummary();
		User fromUser = getAndCheckUserByToken(token);
		if (!atTeam.equals(fromUser.getAtTeam())) {
			throw new InvalidRequestException("User not in team: " + atTeam);
		}

		List<String> toList = reqMsg.getTo();
		if (toList == null || toList.size() == 0) {
			throw new InvalidRequestException("Invalid to: null");
		}

		Set<String> tokens = teamManager.getTokens(atTeam);
		if (tokens == null || tokens.size() == 0) {
			throw new InvalidRequestException("Invalid summary");
		}

		String yyToken = reqMsg.getYytoken();
		int fromYyUid = 0;
		try {
			fromYyUid = yyTokenManager.verifyYyToken(yyToken);
		} catch (InvalidYyTokenException e) {
			throw new InvalidRequestException("Invalid YY token: " + e.getMessage(), e);
		}

		List<User> toUserList = new ArrayList<User>();
		for (String encryptUserId : toList) {

			User userTo = getUserByEncryptUserId(fromUser.getUserType(), encryptUserId);
			if (userTo != null) {
				if (tokens.contains(userTo.getToken())) {
					// user is in team
					toUserList.add(userTo);
				} else {
					// not in team
					log.warn(userTo + " not in team " + atTeam);
				}
			} else {
				// Not found
				log.warn("Cannot find user by type and userid: " + fromUser.getUserType() + ", "
						+ encryptUserId);
			}
		}

		if (log.isInfoEnabled()) {
			log.info("TO user list: " + toUserList + ", with request: " + request);
		}

		if (toUserList.size() == 0) {
			// 2013-11-11 do not throw exception, just log warn, and return ok
			// throw new InvalidRequestException("Invalid to: " + toList);
			log.warn("To User List size is 0, invalid to: " + toList);
		}

		// Call pre send invitation
		if (preSendInvitationPlugins != null && preSendInvitationPlugins.size() != 0) {
			for (PreSendInvitationPlugin plugin : preSendInvitationPlugins) {
				plugin.preSendInvitation(fromUser, toUserList, request);
			}
		}

		// generate response
		SendInvitationResponse response = new SendInvitationResponse();
		SendInvitationResponseMsg respMsg = new SendInvitationResponseMsg();
		respMsg.setInviteId(UUID.randomUUID().toString());
		respMsg.setTimestamp(System.currentTimeMillis());
		response.setMsg(respMsg);

		// store invitation
		Invitation invitation = new Invitation();
		invitation.setInviteId(respMsg.getInviteId());
		invitation.setInviteTimestamp(respMsg.getTimestamp());
		invitation.setFromUserId(fromUser.getUserId());
		invitation.setFromUserType(fromUser.getUserType());
		invitation.setFromDisplayName(fromUser.getDisplayName());
		invitation.setAtTeam(atTeam);
		invitation.setDatas(reqMsg.getDatas());
		invitation.setFromYyUid(fromYyUid);

		Set<InvitationDetail> details = new HashSet<InvitationDetail>();
		for (User userTo : toUserList) {
			InvitationDetail detail = new InvitationDetail();
			detail.setInviteId(respMsg.getInviteId());
			detail.setToUserId(userTo.getUserId());
			detail.setToUserType(userTo.getUserType());
			detail.setToDisplayName(userTo.getDisplayName());
			detail.setReplyTag(InvitationDetail.TAG_NOREAD);
			detail.setNotifyReplyTag(InvitationDetail.TAG_NOREAD);
			detail.setToTag(InvitationDetail.TAG_NOREAD);
			details.add(detail);
		}
		invitation.setDetails(details);

		invitationManager.addInvitation(invitation);

		// send Notify Invitation to users
		for (User userTo : toUserList) {
			String tokenTo = userTo.getToken();
			NotifyInvitationMsgInternal notifyInvitationIntl = new NotifyInvitationMsgInternal();
			NotifyInvitationMsg notifyInvitation = notifyInvitationIntl.getNotifyInvitationMsg();
			notifyInvitationIntl.setFromUserId(fromUser.getUserId());
			notifyInvitationIntl.setFromUserType(fromUser.getUserType());
			notifyInvitation.setInviteId(respMsg.getInviteId());
			notifyInvitation.setInviteTimestamp(respMsg.getTimestamp());
			notifyInvitation.setSummary(atTeam);
			notifyInvitation.setInviteFromEncrypt(fromUser.getUserIdEncrypt());
			notifyInvitation.setFromDisplayName(fromUser.getDisplayName());
			notifyInvitation.setDatas(reqMsg.getDatas());

			String jsonStr = null;
			try {
				jsonStr = objectMapper.writeValueAsString(notifyInvitationIntl);
			} catch (IOException e) {
				// should not happen
				log.error("Seriliaze NotifyInvitationMsgInternal to json failed: " + e, e);
				continue;
			}

			Job job = new Job(NotifyJobTypes.NotifyInvitation, new Object[] { tokenTo, jsonStr });
			queueClient.enqueue(userTo.getAtQueue(), job);
		}

		return response;
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.ServiceManager#sendReply(com.duowan.gamebox.msgcenter.msg.request.SendReplyRequest)
	 */
	@Override
	public SendReplyResponse sendReply(SendReplyRequest request) throws InvalidRequestException,
			ServiceErrorException {
		checkRunningStatus();
		checkRequestMsg(request);
		SendReplyRequestMsg reqMsg = request.getMsg();

		String token = reqMsg.getToken();
		String atTeam = reqMsg.getSummary();
		int tag = reqMsg.getTag();
		User userReplyFrom = getAndCheckUserByToken(token);
		if (tag != InvitationDetail.TAG_ACCEPT && tag != InvitationDetail.TAG_REJECT) {
			throw new InvalidRequestException("Invalid tag: " + tag);
		}

		// get invitation
		String inviteId = reqMsg.getInviteId();
		if (inviteId == null || inviteId.length() == 0) {
			throw new InvalidRequestException("Invalid invite_id: null");
		}
		Invitation invitation = invitationManager.getInvitationById(inviteId);
		if (invitation == null) {
			throw new InvalidRequestException("Invalid invite_id: " + inviteId);
		}

		// check team
		if (!invitation.getAtTeam().equals(atTeam)) {
			throw new InvalidRequestException("Invalid summary: " + atTeam);
		}

		// find and update invitation detail
		// TODO extra, multi times
		boolean findReplayFrom = false;
		for (InvitationDetail detail : invitation.getDetails()) {
			if (detail.getToUserId().equals(userReplyFrom.getUserId())
					&& detail.getToUserType().equals(userReplyFrom.getUserType())) {
				// if (detail.getReplyTag() != InvitationDetail.TAG_NOREAD) {
				// throw new
				// InvalidRequestException("Invitation has replied, invalid tag: "
				// + tag);
				// }

				if (detail.getReplyTag() == tag
						&& euqalString(detail.getReplyExtra(), reqMsg.getExtra())) {
					throw new InvalidRequestException("Invitation has replied, invalid tag: "
							+ tag);
				}
				detail.setReplyTag(tag);
				detail.setReplyTimestamp(System.currentTimeMillis());
				detail.setReplyExtra(reqMsg.getExtra());
				invitationManager.updateInvitationDetail(detail);
				findReplayFrom = true;
				break;
			}
		}
		if (!findReplayFrom) {
			throw new InvalidRequestException("User not in invitation list");
		}

		SendReplyResponse response = new SendReplyResponse();
		SendReplyResponseMsg respMsg = new SendReplyResponseMsg();
		respMsg.setTimestamp(System.currentTimeMillis());
		response.setMsg(respMsg);

		User userInviteFrom = userManager.getUserByIdAndType(invitation.getFromUserId(),
				invitation.getFromUserType());
		if (userInviteFrom != null) {
			// send notify reply to user
			String tokenTo = userInviteFrom.getToken();
			NotifyReplyMsgInternal notifyReplyIntl = new NotifyReplyMsgInternal();
			NotifyReplyMsg notifyReply = notifyReplyIntl.getNotifyReplyMsg();
			notifyReply.setInviteId(reqMsg.getInviteId());
			notifyReplyIntl.setReplyFromUserId(userReplyFrom.getUserId());
			notifyReplyIntl.setReplyFromUserType(userReplyFrom.getUserType());
			notifyReply.setReplyFromUserEncrypt(userReplyFrom.getUserIdEncrypt());
			notifyReply.setReplyFromDisplayName(userReplyFrom.getDisplayName());
			notifyReply.setReplyTimestamp(respMsg.getTimestamp());
			notifyReply.setSummary(atTeam);
			notifyReply.setTag(reqMsg.getTag());
			notifyReply.setInviteTimestamp(invitation.getInviteTimestamp());
			notifyReply.setExtra(reqMsg.getExtra());

			String jsonStr = null;
			try {
				jsonStr = objectMapper.writeValueAsString(notifyReplyIntl);
			} catch (IOException e) {
				// should not happen
				log.error("Seriliaze NotifyReplyMsgInternal to json failed: " + e, e);
				throw new ServiceErrorException("Seriliaze NotifyReplyMsgInternal to json failed: "
						+ e);
			}

			Job job = new Job(NotifyJobTypes.NotifyReply, new Object[] { tokenTo, jsonStr });
			queueClient.enqueue(userInviteFrom.getAtQueue(), job);
		}

		return response;
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.ServiceManager#queryInvitations(com.duowan.gamebox.msgcenter.msg.request.QueryInvitationsRequest)
	 */
	@Override
	public QueryInvitationsResponse queryInvitations(QueryInvitationsRequest request)
			throws InvalidRequestException, ServiceErrorException {
		checkRunningStatus();

		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.ServiceManager#notifyInvitation(com.duowan.gamebox.msgcenter.queue.NotifyInvitation)
	 */
	@Override
	public void notifyInvitation(NotifyInvitation notify) throws ServiceErrorException {
		if (log.isInfoEnabled()) {
			log.info("Invoke notifyInvitation(): " + notify);
		}
		String token = notify.getToken();
		if (token == null || notify.getNotifyInvitationMsgInternal() == null) {
			return;
		}
		AsyncContext asyncContext = mapTokenAsyncContext.get(token);
		if (asyncContext != null) {
			User toUser = userManager.getUserByToken(token);
			sendNotifyInvitationResponse(notify.getNotifyInvitationMsgInternal()
					.getNotifyInvitationMsg(), asyncContext, toUser);
		}
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.ServiceManager#notifyReply(com.duowan.gamebox.msgcenter.queue.NotifyReply)
	 */
	@Override
	public void notifyReply(NotifyReply notify) throws ServiceErrorException {
		if (log.isInfoEnabled()) {
			log.info("Invoke notifyReply(): " + notify);
		}
		String token = notify.getToken();
		NotifyReplyMsgInternal notifyReplyIntl = notify.getNotifyReplyMsgInternal();
		if (token == null || notifyReplyIntl == null) {
			return;
		}
		AsyncContext asyncContext = mapTokenAsyncContext.get(token);
		if (asyncContext != null) {
			User user = userManager.getUserByToken(token);
			sendNotifyReplyResponse(notifyReplyIntl, asyncContext, user);
		}
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.ServiceManager#notifyCloseConnection(com.duowan.gamebox.msgcenter.queue.NotifyCloseConnection)
	 */
	@Override
	public void notifyCloseConnection(NotifyCloseConnection notify) throws ServiceErrorException {
		if (log.isInfoEnabled()) {
			log.info("Invoke notifyCloseConnection(): " + notify);
		}
		String token = notify.getToken();
		if (token != null) {
			AsyncContext asyncContext = mapTokenAsyncContext.get(token);
			if (asyncContext != null) {
				asyncContext.complete();
				if (log.isInfoEnabled()) {
					log.info("Invoke asyncContext.complete() on " + asyncContext + ", with notify "
							+ notify);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.ServiceManager#notifyJoinGame(com.duowan.gamebox.msgcenter.queue.NotifyJoinGame)
	 */
	@Override
	public void notifyJoinGame(NotifyJoinGame notify) throws ServiceErrorException {
		if (log.isInfoEnabled()) {
			log.info("Invoke notifyJoinGame(): " + notify);
		}
		String token = notify.getToken();
		String summary = notify.getSummary();
		if (token == null || summary == null) {
			return;
		}
		AsyncContext asyncContext = mapTokenAsyncContext.get(token);
		if (asyncContext != null) {
			QueryPartnersResponse resp = genQueryPartnersResponse(token, summary);
			try {
				messageSerializer.serializeWithLineBreak(asyncContext.getResponse()
						.getOutputStream(), resp);
				if (log.isInfoEnabled()) {
					User userTo = userManager.getUserByToken(token);
					log.info("Send NotifyJoinGame: " + resp + " to user " + userTo + " with "
							+ asyncContext);
				}
			} catch (IOException e) {
				User userTo = userManager.getUserByToken(token);
				log.error("Send NotifyJoinGame to user " + userTo + " failed: " + e + " with "
						+ asyncContext, e);
				asyncContext.complete();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.ServiceManager#sendUnreadInvitations(com.duowan.gamebox.msgcenter.manager.vo.User)
	 */
	@Override
	public void sendUnreadInvitations(User user) {
		List<NotifyInvitationMsgInternal> msgs = invitationManager.queryUnreadInvitations(user,
				secondsIn);
		if (msgs == null) {
			return;
		}

		AsyncContext asyncContext = mapTokenAsyncContext.get(user.getToken());
		for (NotifyInvitationMsgInternal notiIntl : msgs) {
			try {
				notiIntl.getNotifyInvitationMsg().setInviteFromEncrypt(
						userCryptManager.encryptUserId(
								notiIntl.getFromUserType(), notiIntl.getFromUserId()));
			} catch (GeneralSecurityException e) {
				log.error("encryptUserId failed: " + e, e);
				continue;
			}
			if (asyncContext != null) {
				if (!sendNotifyInvitationResponse(notiIntl.getNotifyInvitationMsg(), asyncContext,
						user)) {
					break;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.ServiceManager#sendUnreadReplies(com.duowan.gamebox.msgcenter.manager.vo.User)
	 */
	@Override
	public void sendUnreadReplies(User user) {
		List<NotifyReplyMsgInternal> msgs = invitationManager.queryUnreadReplies(user,
				secondsIn);
		if (msgs == null) {
			return;
		}

		AsyncContext asyncContext = mapTokenAsyncContext.get(user.getToken());
		for (NotifyReplyMsgInternal notiIntl : msgs) {
			try {
				notiIntl.getNotifyReplyMsg().setReplyFromUserEncrypt(
						userCryptManager.encryptUserId(
								notiIntl.getReplyFromUserType(), notiIntl.getReplyFromUserId()));
			} catch (GeneralSecurityException e) {
				log.error("encryptUserId failed: " + e, e);
				continue;
			}
			if (asyncContext != null) {
				if (!sendNotifyReplyResponse(notiIntl, asyncContext, user)) {
					break;
				}
			}
		}
	}

	private QueryPartnersResponse genQueryPartnersResponse(String token, String theTeam) {
		Set<String> tokens = teamManager.getTokens(theTeam);
		if (tokens != null) {
			tokens.remove(token);
		}
		QueryPartnersResponse response = new QueryPartnersResponse();
		if (tokens != null) {
			QueryPartnersRsponseMsg msg = new QueryPartnersRsponseMsg();
			List<String> partners = new ArrayList<String>();
			msg.setPartners(partners);
			response.setMsg(msg);
			for (String tk : tokens) {
				User user = userManager.getUserByToken(tk);
				partners.add(user.getUserIdEncrypt());
			}
		}
		return response;
	}

	private boolean sendNotifyReplyResponse(NotifyReplyMsgInternal notifyReplyIntl,
			AsyncContext asyncContext, User toUser) {
		boolean notifySuceess = false;
		NotifyReplyResponse notifyResponse = new NotifyReplyResponse();
		NotifyReplyMsg notiMsg = notifyReplyIntl.getNotifyReplyMsg();
		notifyResponse.setMsg(notiMsg);
		try {
			messageSerializer.serializeWithLineBreak(asyncContext.getResponse()
					.getOutputStream(), notifyResponse);
			notifySuceess = true;
			if (log.isInfoEnabled()) {
				log.info("Send NotifyReplyResponse: " + notifyResponse + " to user " + toUser
						+ " on " + asyncContext);
			}
		} catch (IOException e) {
			log.error("Send NotifyReplyResponse to user " + toUser + " on " + asyncContext
					+ " failed: " + e, e);
			asyncContext.complete();
		}

		if (notifySuceess) {
			// update NotifyReplyTag
			Invitation invitation = invitationManager.getInvitationById(notiMsg.getInviteId());
			if (invitation != null) {
				for (InvitationDetail detail : invitation.getDetails()) {
					if (detail.getToUserId().equals(notifyReplyIntl.getReplyFromUserId())
							&& detail.getToUserType()
									.equals(notifyReplyIntl.getReplyFromUserType())) {
						detail.setNotifyReplyTag(InvitationDetail.TAG_READ);
						detail.setNotifyReplyTimestamp(System.currentTimeMillis());
						invitationManager.updateInvitationDetail(detail);
						break;
					}
				}
			}
		}
		return notifySuceess;
	}

	private boolean sendNotifyInvitationResponse(NotifyInvitationMsg notiMsg,
			AsyncContext asyncContext, User toUser) {
		boolean toSuccess = false;
		NotifyInvitationResponse notifyResponse = new NotifyInvitationResponse();
		notifyResponse.setMsg(notiMsg);
		try {
			messageSerializer.serializeWithLineBreak(asyncContext.getResponse()
					.getOutputStream(), notifyResponse);
			if (log.isInfoEnabled()) {
				log.info("Send NotifyInvitationResponse: " + notifyResponse + " to user " + toUser
						+ " on " + asyncContext);
			}
			toSuccess = true;
		} catch (IOException e) {
			log.error("Send NotifyInvitationResponse to user " + toUser + " on " + asyncContext
					+ " failed: " + e, e);
			asyncContext.complete();
		}

		if (toSuccess) {
			// update ToTag
			Invitation invitation = invitationManager.getInvitationById(notiMsg.getInviteId());
			if (invitation != null) {
				for (InvitationDetail detail : invitation.getDetails()) {
					if (detail.getToUserId().equals(toUser.getUserId())
							&& detail.getToUserType().equals(toUser.getUserType())) {
						detail.setToTag(InvitationDetail.TAG_READ);
						detail.setToTimestamp(System.currentTimeMillis());
						invitationManager.updateInvitationDetail(detail);
						break;
					}
				}
			}
		}

		return toSuccess;
	}

	private void startNoopThread() {
		if (noopThread == null) {
			noopRunner = new NoopRunner();
			noopThread = new Thread(noopRunner);
			noopThread.start();
			log.info("Start noop thread");
		}
	}

	private void stopNoopThread() {
		if (noopThread != null) {
			noopRunner.stopMe();
			noopThread.interrupt();
			try {
				noopThread.join(1000L);
			} catch (InterruptedException e) {
			}
			log.info("Stop noop thread");
		}
	}

	private void startNotifyManager() {
		notifyManager.startNotifyWorker();
	}

	private void stopNotifyManager() {
		notifyManager.stopNotifyWorker();
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.ServiceManager#logout(javax.servlet.AsyncContext)
	 */
	@Override
	public void logout(AsyncContext asyncContext) {
		// normal mode
		if (isRunning.get()) {
			String token = mapAsyncContextToken.remove(asyncContext);
			log.info("Invoke logout() " + asyncContext + ", token: " + token);
			if (token != null) {
				mapTokenAsyncContext.remove(token);
				noopDelayQueue.remove(new DelayedUserToken(token));
				User user = userManager.removeUserByToken(token);
				if (user != null) {
					teamManager.removeTokenFromTeam(token, user.getAtTeam());
					notifyMemebersOfTeam(user.getAtTeam());
				}
			}
		}

		// shuting down service, do nothing
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.ServiceManager#sendNoopMessage(javax.servlet.AsyncContext)
	 */
	@Override
	public boolean sendNoopMessage(AsyncContext asyncContext) {
		String token = mapAsyncContextToken.get(asyncContext);
		if (token != null) {
			try {
				messageSerializer.serializeWithLineBreak(asyncContext.getResponse()
						.getOutputStream(),
						noopResponse);
				return true;
			} catch (Exception e) {
				log.warn("sendNoopMessage() on " + asyncContext + " failed: " + e);
				try {
					asyncContext.complete();
				} catch (Exception e2) {
				}
				return false;
			}
		} else {
			try {
				asyncContext.complete();
			} catch (Exception e2) {
			}
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.ServiceManager#startService()
	 */
	@Override
	public void startService() {
		startNoopThread();
		startNotifyManager();
		isRunning.set(true);
		if (log.isInfoEnabled()) {
			log.info("Service Started");
		}
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.ServiceManager#stopService()
	 */
	@Override
	public void stopService() {
		isRunning.set(false);
		stopNoopThread();
		stopNotifyManager();

		for (Map.Entry<AsyncContext, String> entry : mapAsyncContextToken.entrySet()) {
			entry.getKey().complete();
			String token = entry.getValue();
			if (token != null) {
				User user = userManager.removeUserByToken(token);
				if (user != null) {
					teamManager.removeTokenFromTeam(token, user.getAtTeam());
				}
			}
		}
		mapAsyncContextToken.clear();
		mapTokenAsyncContext.clear();
		noopDelayQueue.clear();
		if (log.isInfoEnabled()) {
			log.info("Service Stopped");
		}
	}

	private void checkRunningStatus() throws ServiceErrorException {
		if (isRunning.get()) {
			return;
		} else {
			throw new ServiceErrorException("Service current is unavailable");
		}
	}

	private void checkRequestMsg(AbstractMessage<?> request) throws InvalidRequestException {
		Object msg = request.getMsg();
		if (msg == null) {
			throw new InvalidRequestException("Invalid request: null");
		}
		if (CommonRequestMsg.class.isAssignableFrom(msg.getClass())) {
			CommonRequestMsg commonMsg = (CommonRequestMsg) msg;
			if (commonMsg.getToken() == null) {
				throw new InvalidRequestException("Invalid user token: null");
			}
			if (commonMsg.getSummary() == null) {
				throw new InvalidRequestException("Invalid summary: null");
			}
		}
	}

	private User getAndCheckUserByToken(String token) throws InvalidRequestException {
		if (token != null) {
			User user = userManager.getUserByToken(token);
			if (user != null) {
				return user;
			}
		}
		throw new InvalidRequestException("Invalid user token: " + token);
	}

	private User getUserByEncryptUserId(String type, String encryptUserId) {
		String userId = null;
		try {
			userId = userCryptManager.decryptUserId(type, encryptUserId);
		} catch (GeneralSecurityException e) {
			log.error("Decrypt UserId " + userId + "  failed: " + e);
			return null;
		}

		return userManager.getUserByIdAndType(userId, type);
	}

	private boolean euqalString(String s1, String s2) {
		if (s1 == null) {
			if (s2 != null)
				return false;
		} else if (!s1.equals(s2))
			return false;
		return true;
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
	 * @return the noopDelaySeconds
	 */
	public int getNoopDelaySeconds() {
		return noopDelaySeconds;
	}

	/**
	 * @param noopDelaySeconds
	 *            the noopDelaySeconds to set
	 */
	public void setNoopDelaySeconds(int noopDelaySeconds) {
		this.noopDelaySeconds = noopDelaySeconds;
	}

	/**
	 * @return the userCryptManager
	 */
	public UserCryptManager getUserCryptManager() {
		return userCryptManager;
	}

	/**
	 * @param userCryptManager
	 *            the userCryptManager to set
	 */
	public void setUserCryptManager(UserCryptManager userCryptManager) {
		this.userCryptManager = userCryptManager;
	}

	/**
	 * @return the userManager
	 */
	public UserManager getUserManager() {
		return userManager;
	}

	/**
	 * @param userManager
	 *            the userManager to set
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * @return the notifyManager
	 */
	public NotifyManager getNotifyManager() {
		return notifyManager;
	}

	/**
	 * @param notifyManager
	 *            the notifyManager to set
	 */
	public void setNotifyManager(NotifyManager notifyManager) {
		this.notifyManager = notifyManager;
	}

	/**
	 * @return the teamManager
	 */
	public TeamManager getTeamManager() {
		return teamManager;
	}

	/**
	 * @param teamManager
	 *            the teamManager to set
	 */
	public void setTeamManager(TeamManager teamManager) {
		this.teamManager = teamManager;
	}

	/**
	 * @return the yyTokenManager
	 */
	public YyTokenManager getYyTokenManager() {
		return yyTokenManager;
	}

	/**
	 * @param yyTokenManager
	 *            the yyTokenManager to set
	 */
	public void setYyTokenManager(YyTokenManager yyTokenManager) {
		this.yyTokenManager = yyTokenManager;
	}

	/**
	 * @return the invitationManager
	 */
	public InvitationManager getInvitationManager() {
		return invitationManager;
	}

	/**
	 * @param invitationManager
	 *            the invitationManager to set
	 */
	public void setInvitationManager(InvitationManager invitationManager) {
		this.invitationManager = invitationManager;
	}

	/**
	 * @return the preSendInvitationPlugins
	 */
	public List<PreSendInvitationPlugin> getPreSendInvitationPlugins() {
		return preSendInvitationPlugins;
	}

	/**
	 * @param preSendInvitationPlugins
	 *            the preSendInvitationPlugins to set
	 */
	public void setPreSendInvitationPlugins(List<PreSendInvitationPlugin> preSendInvitationPlugins) {
		this.preSendInvitationPlugins = preSendInvitationPlugins;
	}

	public BytesSerializeClientRedisQueueImpl getSerializeClient() {
		return serializeClient;
	}

	public void setSerializeClient(BytesSerializeClientRedisQueueImpl serializeClient) {
		this.serializeClient = serializeClient;
	}

	class NoopRunner implements Runnable {

		private boolean running = true;

		public void stopMe() {
			this.running = false;
		}

		@Override
		public void run() {

			while (running) {
				DelayedUserToken delayed = null;
				try {
					delayed = noopDelayQueue.take();
				} catch (InterruptedException e) {
					log.info("Catch InterruptedException, current running=" + running);
					continue;
				}
				String token = delayed.getToken();
				AsyncContext asyncContext = mapTokenAsyncContext.get(token);
				if (asyncContext != null) {
					if (sendNoopMessage(asyncContext)) {
						noopDelayQueue.put(new DelayedUserToken(token, noopDelaySeconds));
					}
				}
			}
			log.info("Exit NoopRunner's run()");
		}
	}
}
