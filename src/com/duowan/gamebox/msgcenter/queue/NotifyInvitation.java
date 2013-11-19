package com.duowan.gamebox.msgcenter.queue;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duowan.gamebox.msgcenter.listener.SingletonVars;
import com.duowan.gamebox.msgcenter.manager.exception.ServiceErrorException;
import com.duowan.gamebox.msgcenter.serde.JsonObjectMapper;

public class NotifyInvitation implements Runnable {

	private String token;

	private NotifyInvitationMsgInternal notifyInvitationMsgIntl;

	public NotifyInvitation() {
	}

	public NotifyInvitation(String token, NotifyInvitationMsgInternal notifyInvitationMsgIntl) {
		this.token = token;
		this.notifyInvitationMsgIntl = notifyInvitationMsgIntl;
	}

	public NotifyInvitation(String token, String jsonString) throws IOException {
		this.token = token;
		this.notifyInvitationMsgIntl = JsonObjectMapper.getObjectMapper().readValue(jsonString,
				NotifyInvitationMsgInternal.class);
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the notifyInvitationMsg
	 */
	public NotifyInvitationMsgInternal getNotifyInvitationMsgInternal() {
		return notifyInvitationMsgIntl;
	}

	/**
	 * @param notifyInvitationMsg
	 *            the notifyInvitationMsg to set
	 */
	public void setNotifyInvitationMsgInternal(NotifyInvitationMsgInternal notifyInvitationMsg) {
		this.notifyInvitationMsgIntl = notifyInvitationMsg;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NotifyInvitation [token=").append(token)
				.append(", notifyInvitationMsgIntl=")
				.append(notifyInvitationMsgIntl).append("]");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			SingletonVars.get().getServiceManager().notifyInvitation(this);
		} catch (ServiceErrorException e) {
			Logger log = LoggerFactory.getLogger(this.getClass());
			log.error(e.toString(), e);
		}
	}

}
