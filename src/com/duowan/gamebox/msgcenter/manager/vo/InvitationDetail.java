/**
 * 
 */
package com.duowan.gamebox.msgcenter.manager.vo;

/**
 * @author zhangtao.robin
 * 
 */
public class InvitationDetail {

	public static final int TAG_NOREAD = -1;
	public static final int TAG_REJECT = 0;
	public static final int TAG_ACCEPT = 1;
	public static final int TAG_READ = 2;

	private String inviteId;

	private String toUserId;
	private String toUserType;
	private String toDisplayName;

	private Long replyTimestamp;
	private int replyTag = TAG_NOREAD;
	private Long notifyReplyTimestamp;
	private int notifyReplyTag = TAG_NOREAD;

	private String replyExtra;

	private Long toTimestamp;
	private int toTag = TAG_NOREAD;

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
	 * @return the toUserId
	 */
	public String getToUserId() {
		return toUserId;
	}

	/**
	 * @param toUserId
	 *            the toUserId to set
	 */
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	/**
	 * @return the toUserType
	 */
	public String getToUserType() {
		return toUserType;
	}

	/**
	 * @param toUserType
	 *            the toUserType to set
	 */
	public void setToUserType(String toUserType) {
		this.toUserType = toUserType;
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
	public Long getReplyTimestamp() {
		return replyTimestamp;
	}

	/**
	 * @param replyTimestamp
	 *            the replyTimestamp to set
	 */
	public void setReplyTimestamp(Long replyTimestamp) {
		this.replyTimestamp = replyTimestamp;
	}

	/**
	 * @return the replyTag
	 */
	public int getReplyTag() {
		return replyTag;
	}

	/**
	 * @param replyTag
	 *            the replyTag to set
	 */
	public void setReplyTag(int replyTag) {
		this.replyTag = replyTag;
	}

	/**
	 * @return the notifyReplyTag
	 */
	public int getNotifyReplyTag() {
		return notifyReplyTag;
	}

	/**
	 * @param notifyReplyTag
	 *            the notifyReplyTag to set
	 */
	public void setNotifyReplyTag(int notifyReplyTag) {
		this.notifyReplyTag = notifyReplyTag;
	}

	/**
	 * @return the notifyReplyTimestamp
	 */
	public Long getNotifyReplyTimestamp() {
		return notifyReplyTimestamp;
	}

	/**
	 * @param notifyReplyTimestamp
	 *            the notifyReplyTimestamp to set
	 */
	public void setNotifyReplyTimestamp(Long notifyReplyTimestamp) {
		this.notifyReplyTimestamp = notifyReplyTimestamp;
	}

	/**
	 * @return the replyExtra
	 */
	public String getReplyExtra() {
		return replyExtra;
	}

	/**
	 * @param replyExtra
	 *            the replyExtra to set
	 */
	public void setReplyExtra(String replyExtra) {
		this.replyExtra = replyExtra;
	}

	/**
	 * @return the toTimestamp
	 */
	public Long getToTimestamp() {
		return toTimestamp;
	}

	/**
	 * @param toTimestamp
	 *            the toTimestamp to set
	 */
	public void setToTimestamp(Long toTimestamp) {
		this.toTimestamp = toTimestamp;
	}

	/**
	 * @return the toTag
	 */
	public int getToTag() {
		return toTag;
	}

	/**
	 * @param toTag
	 *            the toTag to set
	 */
	public void setToTag(int toTag) {
		this.toTag = toTag;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InvitationDetail [inviteId=").append(inviteId).append(", toUserId=")
				.append(toUserId).append(", toUserType=").append(toUserType)
				.append(", toDisplayName=").append(toDisplayName).append(", replyTimestamp=")
				.append(replyTimestamp).append(", replyTag=").append(replyTag)
				.append(", notifyReplyTimestamp=").append(notifyReplyTimestamp)
				.append(", notifyReplyTag=").append(notifyReplyTag).append(", replyExtra=")
				.append(replyExtra).append(", toTimestamp=").append(toTimestamp).append(", toTag=")
				.append(toTag).append("]");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inviteId == null) ? 0 : inviteId.hashCode());
		result = prime * result + ((toUserId == null) ? 0 : toUserId.hashCode());
		result = prime * result + ((toUserType == null) ? 0 : toUserType.hashCode());
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
		InvitationDetail other = (InvitationDetail) obj;
		if (inviteId == null) {
			if (other.inviteId != null)
				return false;
		} else if (!inviteId.equals(other.inviteId))
			return false;
		if (toUserId == null) {
			if (other.toUserId != null)
				return false;
		} else if (!toUserId.equals(other.toUserId))
			return false;
		if (toUserType == null) {
			if (other.toUserType != null)
				return false;
		} else if (!toUserType.equals(other.toUserType))
			return false;
		return true;
	}

}
