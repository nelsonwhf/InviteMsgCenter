/**
 * 
 */
package com.duowan.gamebox.msgcenter.msg.vo;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author zhangtao.robin
 * 
 */
public class SendReplyRequestMsg extends CommonRequestMsg {

	@JsonProperty(value = "invite_id")
	private String inviteId;

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
		builder.append("SendReplyRequestMsg [inviteId=").append(inviteId).append(", tag=")
				.append(tag).append(", extra=").append(extra).append("]");
		return builder.toString();
	}

}
