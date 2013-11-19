package com.duowan.gamebox.msgcenter.msg.vo;

import java.util.List;

public class QueryInvitationsResponseMsg {

	private int total;

	private int unread;

	private List<InvitationInfo> docs;

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * @return the unread
	 */
	public int getUnread() {
		return unread;
	}

	/**
	 * @param unread
	 *            the unread to set
	 */
	public void setUnread(int unread) {
		this.unread = unread;
	}

	/**
	 * @return the docs
	 */
	public List<InvitationInfo> getDocs() {
		return docs;
	}

	/**
	 * @param docs
	 *            the docs to set
	 */
	public void setDocs(List<InvitationInfo> docs) {
		this.docs = docs;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("QueryInvitationsResponseMsg [total=").append(total).append(", unread=")
				.append(unread).append(", docs=").append(docs).append("]");
		return builder.toString();
	}

}
