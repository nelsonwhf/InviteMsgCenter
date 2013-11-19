package com.duowan.gamebox.msgcenter.msg.vo;

import org.codehaus.jackson.annotate.JsonProperty;

public class InvitationInfo {

	@JsonProperty(value = "invite_id")
	private String inviteId;

	@JsonProperty(value = "invite_timestamp")
	private long inviteTimestamp;

	private String from;

	@JsonProperty(value = "from_display_name")
	private String fromDisplayName;

	private String to;

	@JsonProperty(value = "to_display_name")
	private String toDisplayName;

	@JsonProperty(value = "reply_timestamp")
	private long replyTimestamp;;

	private String datas;

	private int tag;

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
	 * @return the inviteTimestamp
	 */
	public long getInviteTimestamp() {
		return inviteTimestamp;
	}

	/**
	 * @param inviteTimestamp
	 *            the inviteTimestamp to set
	 */
	public void setInviteTimestamp(long inviteTimestamp) {
		this.inviteTimestamp = inviteTimestamp;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the fromDisplayName
	 */
	public String getFromDisplayName() {
		return fromDisplayName;
	}

	/**
	 * @param fromDisplayName
	 *            the fromDisplayName to set
	 */
	public void setFromDisplayName(String fromDisplayName) {
		this.fromDisplayName = fromDisplayName;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the toDisplayName
	 */
	public String getToDisplayName() {
		return toDisplayName;
	}

	/**
	 * @param toDisplayName
	 *            the toDisplayName to set
	 */
	public void setToDisplayName(String toDisplayName) {
		this.toDisplayName = toDisplayName;
	}

	/**
	 * @return the replyTimestamp
	 */
	public long getReplyTimestamp() {
		return replyTimestamp;
	}

	/**
	 * @param replyTimestamp
	 *            the replyTimestamp to set
	 */
	public void setReplyTimestamp(long replyTimestamp) {
		this.replyTimestamp = replyTimestamp;
	}

	/**
	 * @return the datas
	 */
	public String getDatas() {
		return datas;
	}

	/**
	 * @param datas
	 *            the datas to set
	 */
	public void setDatas(String datas) {
		this.datas = datas;
	}

	/**
	 * @return the tag
	 */
	public int getTag() {
		return tag;
	}

	/**
	 * @param tag
	 *            the tag to set
	 */
	public void setTag(int tag) {
		this.tag = tag;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InvitationInfo [inviteId=").append(inviteId).append(", inviteTimestamp=")
				.append(inviteTimestamp).append(", from=").append(from)
				.append(", fromDisplayName=").append(fromDisplayName).append(", to=").append(to)
				.append(", toDisplayName=").append(toDisplayName).append(", replyTimestamp=")
				.append(replyTimestamp).append(", datas=").append(datas).append(", tag=")
				.append(tag).append("]");
		return builder.toString();
	}

}
