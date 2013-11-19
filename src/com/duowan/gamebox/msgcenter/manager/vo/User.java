package com.duowan.gamebox.msgcenter.manager.vo;

public class User {

	private String token;
	private String userId;
	private String userType;
	private String displayName;
	private String userIdEncrypt;
	private String atQueue;
	private String atTeam;

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
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * @param userType
	 *            the userType to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the userIdEncrypt
	 */
	public String getUserIdEncrypt() {
		return userIdEncrypt;
	}

	/**
	 * @param userIdEncrypt
	 *            the userUidEncrypt to set
	 */
	public void setUserIdEncrypt(String userIdEncrypt) {
		this.userIdEncrypt = userIdEncrypt;
	}

	/**
	 * @return the atQueue
	 */
	public String getAtQueue() {
		return atQueue;
	}

	/**
	 * @param atQueue
	 *            the atQueue to set
	 */
	public void setAtQueue(String atQueue) {
		this.atQueue = atQueue;
	}

	/**
	 * @return the atTeam
	 */
	public String getAtTeam() {
		return atTeam;
	}

	/**
	 * @param atTeam
	 *            the atTeam to set
	 */
	public void setAtTeam(String atTeam) {
		this.atTeam = atTeam;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [token=").append(token).append(", userId=").append(userId)
				.append(", userType=").append(userType).append(", displayName=")
				.append(displayName).append(", userUidEncrypt=").append(userIdEncrypt)
				.append(", atQueue=").append(atQueue).append(", atTeam=").append(atTeam)
				.append("]");
		return builder.toString();
	}

}
