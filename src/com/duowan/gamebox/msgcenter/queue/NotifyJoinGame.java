package com.duowan.gamebox.msgcenter.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duowan.gamebox.msgcenter.listener.SingletonVars;
import com.duowan.gamebox.msgcenter.manager.exception.ServiceErrorException;

public class NotifyJoinGame implements Runnable {

	private String token;

	private String summary;

	public NotifyJoinGame() {
	}

	public NotifyJoinGame(String token, String summary) {
		this.token = token;
		this.summary = summary;
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
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @param summary
	 *            the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NotifyJoinGame [token=").append(token).append(", summary=").append(summary)
				.append("]");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			SingletonVars.get().getServiceManager().notifyJoinGame(this);
		} catch (ServiceErrorException e) {
			Logger log = LoggerFactory.getLogger(this.getClass());
			log.error(e.toString(), e);
		}
	}

}
