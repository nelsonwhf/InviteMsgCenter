/**
 * 
 */
package com.duowan.gamebox.msgcenter.manager.exception;

/**
 * @author zhangtao.robin
 * 
 */
public class InvalidRequestWithCodeException extends InvalidRequestException {

	private static final long serialVersionUID = -8305135796060900786L;

	private int code;

	/**
	 * @param code
	 */
	public InvalidRequestWithCodeException(int code) {
		super();
		this.code = code;
	}

	/**
	 * @param code
	 * @param message
	 * @param cause
	 */
	public InvalidRequestWithCodeException(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	/**
	 * @param code
	 * @param message
	 */
	public InvalidRequestWithCodeException(int code, String message) {
		super(message);
		this.code = code;
	}

	/**
	 * @param code
	 * @param cause
	 */
	public InvalidRequestWithCodeException(int code, Throwable cause) {
		super(cause);
		this.code = code;
	}

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
}
