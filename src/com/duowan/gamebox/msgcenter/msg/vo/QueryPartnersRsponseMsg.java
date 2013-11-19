package com.duowan.gamebox.msgcenter.msg.vo;

import java.util.List;

public class QueryPartnersRsponseMsg {

	private List<String> partners;

	/**
	 * @return the partners
	 */
	public List<String> getPartners() {
		return partners;
	}

	/**
	 * @param partners
	 *            the partners to set
	 */
	public void setPartners(List<String> partners) {
		this.partners = partners;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("QueryInvitationRsponseMsg [partners=").append(partners).append("]");
		return builder.toString();
	}

}
