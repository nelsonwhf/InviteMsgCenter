package com.duowan.gamebox.msgcenter.msg.vo;

import org.codehaus.jackson.annotate.JsonProperty;

public class NotifyInvitationMsg {

	@JsonProperty(value = "invite_id")
	private String inviteId;

	private String summary;

	@JsonProperty(value = "timestamp")
	private long inviteTimestamp;

	@JsonProperty(value = "invite_from")
	private String inviteFromEncrypt;

	@JsonProperty(value = "display_name")
	private String fromDisplayName;

	private String datas;

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
	 * @return the inviteFromEncrypt
	 */
	public String getInviteFromEncrypt() {
		return inviteFromEncrypt;
	}

	/**
	 * @param inviteFromEncrypt
	 *            the inviteFromEncrypt to set
	 */
	public void setInviteFromEncrypt(String inviteFrom) {
		this.inviteFromEncrypt = inviteFrom;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NotifyInvitationMsg [inviteId=").append(inviteId).append(", summary=")
				.append(summary).append(", inviteTimestamp=").append(inviteTimestamp)
				.append(", inviteFromEncrypt=").append(inviteFromEncrypt)
				.append(", fromDisplayName=")
				.append(fromDisplayName).append(", datas=").append(datas).append("]");
		return builder.toString();
	}

}
