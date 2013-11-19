package com.duowan.gamebox.msgcenter.msg.vo;

public class SendReplyResponseMsg {
	private long timestamp;

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
		builder.append("SendReplyResponsetMsg [timestamp=").append(timestamp).append("]");
		return builder.toString();
	}

}
