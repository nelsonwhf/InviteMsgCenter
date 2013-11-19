/**
 * 
 */
package com.duowan.gamebox.msgcenter.manager.exception;

/**
 * @author zhangtao.robin
 * 
 */
public class AuthorizeErrorException extends Exception {

	private static final long serialVersionUID = -3822379434484978095L;

	/**
	 * 
	 */
	public AuthorizeErrorException() {
	}

	/**
	 * @param message
	 */
	public AuthorizeErrorException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public AuthorizeErrorException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AuthorizeErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public AuthorizeErrorException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
