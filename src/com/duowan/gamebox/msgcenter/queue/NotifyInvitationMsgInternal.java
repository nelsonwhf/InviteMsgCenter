package com.duowan.gamebox.msgcenter.queue;

import com.duowan.gamebox.msgcenter.msg.vo.NotifyInvitationMsg;

public class NotifyInvitationMsgInternal {

	private NotifyInvitationMsg notifyInvitationMsg = new NotifyInvitationMsg();

	private String fromUserId;

	private String fromUserType;

	/**
	 * @return the notifyInvitationMsg
	 */
	public NotifyInvitationMsg getNotifyInvitationMsg() {
		return notifyInvitationMsg;
	}

	/**
	 * @param notifyInvitationMsg
	 *            the notifyInvitationMsg to set
	 */
	public void setNotifyInvitationMsg(NotifyInvitationMsg notifyInvitationMsg) {
		this.notifyInvitationMsg = notifyInvitationMsg;
	}

	/**
	 * @return the fromUserId
	 */
	public String getFromUserId() {
		return fromUserId;
	}

	/**
	 * @param fromUserId
	 *            the fromUserId to set
	 */
	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	/**
	 * @return the fromUserType
	 */
	public String getFromUserType() {
		return fromUserType;
	}

	/**
	 * @param fromUserType
	 *            the fromUserType to set
	 */
	public void setFromUserType(String fromUserType) {
		this.fromUserType = fromUserType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NotifyInvitationMsgInternal [notifyInvitationMsg=")
				.append(notifyInvitationMsg).append(", fromUserId=").append(fromUserId)
				.append(", fromUserType=").append(fromUserType).append("]");
		return builder.toString();
	}

}
