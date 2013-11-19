package com.duowan.gamebox.msgcenter.queue;

import com.duowan.gamebox.msgcenter.msg.vo.NotifyReplyMsg;

public class NotifyReplyMsgInternal {
	private String replyFromUserId;

	private String replyFromUserType;

	private NotifyReplyMsg notifyReplyMsg = new NotifyReplyMsg();

	/**
	 * @return the replyFromUserId
	 */
	public String getReplyFromUserId() {
		return replyFromUserId;
	}

	/**
	 * @param replyFromUserId
	 *            the replyFromUserId to set
	 */
	public void setReplyFromUserId(String replyFromUserId) {
		this.replyFromUserId = replyFromUserId;
	}

	/**
	 * @return the replyFromUserType
	 */
	public String getReplyFromUserType() {
		return replyFromUserType;
	}

	/**
	 * @param replyFromUserType
	 *            the replyFromUserType to set
	 */
	public void setReplyFromUserType(String replyFromUserType) {
		this.replyFromUserType = replyFromUserType;
	}

	/**
	 * @return the notifyReplyMsg
	 */
	public NotifyReplyMsg getNotifyReplyMsg() {
		return notifyReplyMsg;
	}

	/**
	 * @param notifyReplyMsg
	 *            the notifyReplyMsg to set
	 */
	public void setNotifyReplyMsg(NotifyReplyMsg notifyReplyMsg) {
		this.notifyReplyMsg = notifyReplyMsg;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NotifyReplyMsgInternal [replyFromUserId=").append(replyFromUserId)
				.append(", replyFromUserType=").append(replyFromUserType)
				.append(", notifyReplyMsg=").append(notifyReplyMsg).append("]");
		return builder.toString();
	}

}
