/**
 * 
 */
package com.duowan.gamebox.msgcenter.manager.exception;

/**
 * @author zhangtao.robin
 * 
 */
public class ServiceErrorException extends Exception {

	private static final long serialVersionUID = 4340034287422488101L;

	/**
	 * 
	 */
	public ServiceErrorException() {
	}

	/**
	 * @param message
	 */
	public ServiceErrorException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ServiceErrorException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServiceErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ServiceErrorException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
