/**
 * 
 */
package com.duowan.gamebox.msgcenter.redis;

import javax.servlet.ServletContext;

/**
 * @author zhangtao.robin
 * 
 */
public class RedisConfig {
	private String redisHost;
	private int redisPort = 6379;
	private int redisDb = 0;
	private String redisPassword;
	private int redisTimeout = 5000;

	public RedisConfig() {
	}

	public RedisConfig(ServletContext context) {
		setRedisHost(getParamString(context, "redisHost", "183.61.12.145"));
		setRedisPort(getParamInt(context, "redisPort", 6379));
		setRedisDb(getParamInt(context, "redisDb", 0));
		setRedisPassword(getParamString(context, "redisPassword"));
		setRedisTimeout(getParamInt(context, "redisTimeout", 5000));
	}

	private String getParamString(ServletContext context, String key) {
		return getParamString(context, key, null);
	}

	private String getParamString(ServletContext context, String key, String defaultValue) {
		String s = context.getInitParameter(key);
		return s != null ? s : defaultValue;
	}

	private int getParamInt(ServletContext context, String key, int defaultValue) {
		String s = context.getInitParameter(key);
		return s != null ? Integer.valueOf(s) : defaultValue;
	}

	/**
	 * @return the redisHost
	 */
	public String getRedisHost() {
		return redisHost;
	}

	/**
	 * @param redisHost
	 *            the redisHost to set
	 */
	public void setRedisHost(String redisHost) {
		this.redisHost = redisHost;
	}

	/**
	 * @return the redisPort
	 */
	public int getRedisPort() {
		return redisPort;
	}

	/**
	 * @param redisPort
	 *            the redisPort to set
	 */
	public void setRedisPort(int redisPort) {
		this.redisPort = redisPort;
	}

	/**
	 * @return the redisDb
	 */
	public int getRedisDb() {
		return redisDb;
	}

	/**
	 * @param redisDb
	 *            the redisDb to set
	 */
	public void setRedisDb(int redisDb) {
		this.redisDb = redisDb;
	}

	/**
	 * @return the redisPassword
	 */
	public String getRedisPassword() {
		return redisPassword;
	}

	/**
	 * @param redisPassword
	 *            the redisPassword to set
	 */
	public void setRedisPassword(String redisPassword) {
		this.redisPassword = redisPassword;
	}

	/**
	 * @return the redisTimeout
	 */
	public int getRedisTimeout() {
		return redisTimeout;
	}

	/**
	 * @param redisTimeout
	 *            the redisTimeout to set
	 */
	public void setRedisTimeout(int redisTimeout) {
		this.redisTimeout = redisTimeout;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RedisConfig [redisHost=").append(redisHost).append(", redisPort=")
				.append(redisPort).append(", redisDb=").append(redisDb).append(", redisPassword=")
				.append(redisPassword).append(", redisTimeout=").append(redisTimeout).append("]");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + redisDb;
		result = prime * result + ((redisHost == null) ? 0 : redisHost.hashCode());
		result = prime * result + ((redisPassword == null) ? 0 : redisPassword.hashCode());
		result = prime * result + redisPort;
		result = prime * result + redisTimeout;
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
		RedisConfig other = (RedisConfig) obj;
		if (redisDb != other.redisDb)
			return false;
		if (redisHost == null) {
			if (other.redisHost != null)
				return false;
		} else if (!redisHost.equals(other.redisHost))
			return false;
		if (redisPassword == null) {
			if (other.redisPassword != null)
				return false;
		} else if (!redisPassword.equals(other.redisPassword))
			return false;
		if (redisPort != other.redisPort)
			return false;
		if (redisTimeout != other.redisTimeout)
			return false;
		return true;
	}

}
