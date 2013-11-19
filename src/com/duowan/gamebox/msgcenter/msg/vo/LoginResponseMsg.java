package com.duowan.gamebox.msgcenter.msg.vo;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.duowan.gamebox.msgcenter.manager.vo.User;

public class LoginResponseMsg {

	private String token;

	@JsonIgnore
	private User user;

	public LoginResponseMsg() {
	}

	public LoginResponseMsg(String token, User user) {
		this.token = token;
		this.user = user;
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
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoginResponseMsg [token=").append(token).append("]");
		return builder.toString();
	}

}
