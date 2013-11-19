package com.duowan.gamebox.msgcenter.msg.vo;

import org.codehaus.jackson.annotate.JsonProperty;

public class QueryInvitationsRequestMsg {

	private String token;

	@JsonProperty(value = "typ")
	private SendRecvType queryType;

	private int start;

	private int size;

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
	 * @return the queryType
	 */
	public SendRecvType getQueryType() {
		return queryType;
	}

	/**
	 * @param queryType
	 *            the queryType to set
	 */
	public void setQueryType(SendRecvType queryType) {
		this.queryType = queryType;
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("QueryInvitationRequestMsg [token=").append(token).append(", queryType=")
				.append(queryType).append(", start=").append(start).append(", size=").append(size)
				.append("]");
		return builder.toString();
	}

}
