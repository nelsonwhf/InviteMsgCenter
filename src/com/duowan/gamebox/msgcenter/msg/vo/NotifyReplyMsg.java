package com.duowan.gamebox.msgcenter.msg.vo;

import org.codehaus.jackson.annotate.JsonProperty;

public class NotifyReplyMsg {

	@JsonProperty(value = "invite_id")
	private String inviteId;

	private String summary;

	@JsonProperty(value = "invite_timestamp")
	private long inviteTimestamp;

	@JsonProperty(value = "reply_from")
	private String replyFromUserEncrypt;

	@JsonProperty(value = "display_name")
	private String replyFromDisplayName;

	@JsonProperty(value = "timestamp")
	private long replyTimestamp;

	private int tag;

	private String extra;

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
	 * @return the replyFromDisplayName
	 */
	public String getReplyFromDisplayName() {
		return replyFromDisplayName;
	}

	/**
	 * @param replyFromDisplayName
	 *            the replyFromDisplayName to set
	 */
	public void setReplyFromDisplayName(String replyFromDisplayName) {
		this.replyFromDisplayName = replyFromDisplayName;
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

	/**
	 * @return the replyFromUserEncrypt
	 */
	public String getReplyFromUserEncrypt() {
		return replyFromUserEncrypt;
	}

	/**
	 * @param replyFromUserEncrypt
	 *            the replyFromUserEncrypt to set
	 */
	public void setReplyFromUserEncrypt(String replyFromUserEncrypt) {
		this.replyFromUserEncrypt = replyFromUserEncrypt;
	}

	/**
	 * @return the extra
	 */
	public String getExtra() {
		return extra;
	}

	/**
	 * @param extra
	 *            the extra to set
	 */
	public void setExtra(String extra) {
		this.extra = extra;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NotifyReplyMsg [inviteId=").append(inviteId).append(", summary=")
				.append(summary).append(", inviteTimestamp=").append(inviteTimestamp)
				.append(", replyFromUserEncrypt=").append(replyFromUserEncrypt)
				.append(", replyFromDisplayName=").append(replyFromDisplayName)
				.append(", replyTimestamp=").append(replyTimestamp).append(", tag=").append(tag)
				.append(", extra=").append(extra).append("]");
		return builder.toString();
	}

}
