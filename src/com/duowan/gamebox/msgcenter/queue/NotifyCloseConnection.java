package com.duowan.gamebox.msgcenter.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duowan.gamebox.msgcenter.listener.SingletonVars;
import com.duowan.gamebox.msgcenter.manager.exception.ServiceErrorException;

public class NotifyCloseConnection implements Runnable {

	private String token;

	public NotifyCloseConnection() {
	}

	public NotifyCloseConnection(String token) {
		this.token = token;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NotifyClosePrevConnection [token=").append(token).append("]");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			SingletonVars.get().getServiceManager().notifyCloseConnection(this);
		} catch (ServiceErrorException e) {
			Logger log = LoggerFactory.getLogger(this.getClass());
			log.error(e.toString(), e);
		}
	}

}
