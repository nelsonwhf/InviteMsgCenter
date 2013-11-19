package com.duowan.gamebox.msgcenter.msg.vo;

public class ErrorMsg {

	private String error;

	public ErrorMsg() {
	}

	public ErrorMsg(String error) {
		this.error = error;
	}

	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param error
	 *            the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ErrorMsg [error=").append(error).append("]");
		return builder.toString();
	}

}
