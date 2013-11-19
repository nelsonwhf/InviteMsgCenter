/**
 * 
 */
package com.duowan.gamebox.msgcenter.msg;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author zhangtao.robin
 * 
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public abstract class AbstractMessage<T> {

	public static final int LOGIN_CODE = 200;
	public static final int JOIN_GAME_CODE = 201;
	public static final int QUERY_PARTNERS_CODE = 202;
	public static final int SEND_INVITATION_CODE = 203;
	public static final int NOTIFY_INVITATION_CODE = 204;
	public static final int SEND_REPLY_CODE = 205;
	public static final int NOTIFY_REPLY_CODE = 206;
	public static final int QUERY_INVITATIONS_CODE = 207;
	public static final int NOOP_CODE = 210;
	public static final int SERVER_LIST_CODE = 211;

	public static final int ERROR_REQUEST_CODE = 400;
	public static final int ERROR_NONAUTH_CODE = 401;
	public static final int ERROR_FORBIDDEN_CODE = 403;
	public static final int ERROR_INTERNAL_CODE = 500;

	public static final String LINE_SPLITTER = "\r\n";
	public static final byte[] LINE_SPLITTER_BYTES = new byte[] { 0x0D, 0x0A };

	private int code;
	private T msg;

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the msg
	 */
	public T getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(T msg) {
		this.msg = msg;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName()).append(" [code=").append(code).append(", msg=")
				.append(msg)
				.append("]");
		return builder.toString();
	}

}
