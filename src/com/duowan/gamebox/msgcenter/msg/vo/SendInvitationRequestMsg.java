/**
 * 
 */
package com.duowan.gamebox.msgcenter.msg.vo;

import java.util.List;

/**
 * @author zhangtao.robin
 * 
 */
public class SendInvitationRequestMsg extends CommonRequestMsg {

	private String yytoken;
	private List<String> to;
	private String datas;
	private String tid; // added at 2013-11-07

	/**
	 * @return the yytoken
	 */
	public String getYytoken() {
		return yytoken;
	}

	/**
	 * @param yytoken
	 *            the yytoken to set
	 */
	public void setYytoken(String yytoken) {
		this.yytoken = yytoken;
	}

	/**
	 * @return the to
	 */
	public List<String> getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(List<String> to) {
		this.to = to;
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
	 * @return the tid
	 */
	public String getTid() {
		return tid;
	}

	/**
	 * @param tid
	 *            the tid to set
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SendInvitationRequestMsg [yytoken=").append(yytoken).append(", to=")
				.append(to).append(", tid=").append(tid).append(", datas=").append(datas)
				.append(", super=").append(super.toString()).append("]");
		return builder.toString();
	}

}
