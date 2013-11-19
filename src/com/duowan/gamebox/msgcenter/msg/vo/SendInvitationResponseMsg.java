package com.duowan.gamebox.msgcenter.msg.vo;

import org.codehaus.jackson.annotate.JsonProperty;

public class SendInvitationResponseMsg {

	@JsonProperty(value = "invite_id")
	private String inviteId;

	private long timestamp;

	/**
	 * @return the inviteId
	 */
	public String getInviteId() {
		return inviteId;
	}

	/**
	 * @param inviteId
	 *            the inviteId to set
	 */
	public void setInviteId(String inviteId) {
		this.inviteId = inviteId;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SendInvitationResponseMsg [inviteId=").append(inviteId)
				.append(", timestamp=").append(timestamp).append("]");
		return builder.toString();
	}

}
