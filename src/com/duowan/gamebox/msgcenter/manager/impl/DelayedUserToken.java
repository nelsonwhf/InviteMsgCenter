/**
 * 
 */
package com.duowan.gamebox.msgcenter.manager.impl;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangtao.robin
 * 
 */
public class DelayedUserToken implements Delayed {

	private String token;
	private volatile long triggerTime;

	public DelayedUserToken(String token) {
		super();
		this.token = token;
	}

	public DelayedUserToken(String token, int delaySeconds) {
		super();
		this.token = token;
		triggerTime = System.currentTimeMillis()
				+ TimeUnit.MILLISECONDS.convert(delaySeconds, TimeUnit.SECONDS);
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

	@Override
	public int compareTo(Delayed o) {
		DelayedUserToken that = (DelayedUserToken) o;
		if (triggerTime < that.triggerTime)
			return -1;
		if (triggerTime > that.triggerTime)
			return 1;
		return 0;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(triggerTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DelayedUserToken other = (DelayedUserToken) obj;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}

}
