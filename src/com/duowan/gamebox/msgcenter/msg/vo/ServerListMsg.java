package com.duowan.gamebox.msgcenter.msg.vo;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ServerListMsg {

	@JsonProperty(value = "loop_urls")
	private List<String> loopUrls;

	@JsonProperty(value = "request_urls")
	private List<String> requestUrls;

	/**
	 * @return the loopUrls
	 */
	public List<String> getLoopUrls() {
		return loopUrls;
	}

	/**
	 * @param loopUrls
	 *            the loopUrls to set
	 */
	public void setLoopUrls(List<String> loopUrls) {
		this.loopUrls = loopUrls;
	}

	/**
	 * @return the requestUrls
	 */
	public List<String> getRequestUrls() {
		return requestUrls;
	}

	/**
	 * @param requestUrls
	 *            the requestUrls to set
	 */
	public void setRequestUrls(List<String> requestUrls) {
		this.requestUrls = requestUrls;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ServerListMsg [loopUrls=").append(loopUrls).append(", requestUrls=")
				.append(requestUrls).append("]");
		return builder.toString();
	}

}
