package com.duowan.gamebox.msgcenter.manager;

import javax.servlet.AsyncContext;

import com.duowan.gamebox.msgcenter.manager.exception.AuthorizeErrorException;
import com.duowan.gamebox.msgcenter.manager.exception.InvalidRequestException;
import com.duowan.gamebox.msgcenter.manager.exception.ServiceErrorException;
import com.duowan.gamebox.msgcenter.manager.vo.User;
import com.duowan.gamebox.msgcenter.msg.request.JoinGameRequest;
import com.duowan.gamebox.msgcenter.msg.request.LoginRequest;
import com.duowan.gamebox.msgcenter.msg.request.QueryInvitationsRequest;
import com.duowan.gamebox.msgcenter.msg.request.QueryPartnersRequest;
import com.duowan.gamebox.msgcenter.msg.request.SendInvitationRequest;
import com.duowan.gamebox.msgcenter.msg.request.SendReplyRequest;
import com.duowan.gamebox.msgcenter.msg.response.JoinGameResponse;
import com.duowan.gamebox.msgcenter.msg.response.LoginResponse;
import com.duowan.gamebox.msgcenter.msg.response.QueryInvitationsResponse;
import com.duowan.gamebox.msgcenter.msg.response.QueryPartnersResponse;
import com.duowan.gamebox.msgcenter.msg.response.SendInvitationResponse;
import com.duowan.gamebox.msgcenter.msg.response.SendReplyResponse;
import com.duowan.gamebox.msgcenter.queue.NotifyCloseConnection;
import com.duowan.gamebox.msgcenter.queue.NotifyInvitation;
import com.duowan.gamebox.msgcenter.queue.NotifyJoinGame;
import com.duowan.gamebox.msgcenter.queue.NotifyReply;

/**
 * @author zhangtao.robin
 * 
 */
public interface ServiceManager {

	public LoginResponse login(LoginRequest request, AsyncContext asyncContext)
			throws InvalidRequestException, ServiceErrorException, AuthorizeErrorException;

	public JoinGameResponse joinGame(JoinGameRequest request) throws InvalidRequestException,
			ServiceErrorException;

	public QueryPartnersResponse queryPartners(QueryPartnersRequest request)
			throws InvalidRequestException, ServiceErrorException;

	public SendInvitationResponse sendInvitation(SendInvitationRequest request)
			throws InvalidRequestException, ServiceErrorException;

	public SendReplyResponse sendReply(SendReplyRequest request) throws InvalidRequestException,
			ServiceErrorException;

	public QueryInvitationsResponse queryInvitations(QueryInvitationsRequest request)
			throws InvalidRequestException, ServiceErrorException;

	public void notifyInvitation(NotifyInvitation notify) throws ServiceErrorException;

	public void notifyReply(NotifyReply notify) throws ServiceErrorException;

	public void notifyJoinGame(NotifyJoinGame notify) throws ServiceErrorException;

	public void notifyCloseConnection(NotifyCloseConnection notify) throws ServiceErrorException;

	public void logout(AsyncContext asyncContext);

	public boolean sendNoopMessage(AsyncContext asyncContext);

	public void sendUnreadInvitations(User user);

	public void sendUnreadReplies(User user);

	public void startService();

	public void stopService();
}
