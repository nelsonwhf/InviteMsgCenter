package com.duowan.gamebox.msgcenter.queue;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duowan.gamebox.msgcenter.listener.SingletonVars;
import com.duowan.gamebox.msgcenter.manager.exception.ServiceErrorException;
import com.duowan.gamebox.msgcenter.serde.JsonObjectMapper;

public class NotifyReply implements Runnable {

	private String token;
	private NotifyReplyMsgInternal notifyReplyIntl;

	public NotifyReply() {
	}

	public NotifyReply(String token, NotifyReplyMsgInternal notifyReplyIntl) {
		this.token = token;
		this.notifyReplyIntl = notifyReplyIntl;
	}

	public NotifyReply(String token, String jsonString) throws IOException {
		this.token = token;
		this.notifyReplyIntl = JsonObjectMapper.getObjectMapper().readValue(jsonString,
				NotifyReplyMsgInternal.class);
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
	 * @return the notifyReplyMsg
	 */
	public NotifyReplyMsgInternal getNotifyReplyMsgInternal() {
		return notifyReplyIntl;
	}

	/**
	 * @param notifyReplyMsg
	 *            the notifyReplyMsg to set
	 */
	public void setNotifyReplyMsgInternal(NotifyReplyMsgInternal notifyReplyMsg) {
		this.notifyReplyIntl = notifyReplyMsg;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NotifyReply [token=").append(token).append(", notifyReplyIntl=")
				.append(notifyReplyIntl).append("]");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			SingletonVars.get().getServiceManager().notifyReply(this);
		} catch (ServiceErrorException e) {
			Logger log = LoggerFactory.getLogger(this.getClass());
			log.error(e.toString(), e);
		}
	}

}
