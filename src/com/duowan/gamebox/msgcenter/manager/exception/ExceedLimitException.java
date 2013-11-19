/**
 * 
 */
package com.duowan.gamebox.msgcenter.manager.exception;

/**
 * @author zhangtao.robin
 * 
 */
public class ExceedLimitException extends Exception {

	private static final long serialVersionUID = 6812395544169640306L;

	/**
	 * 
	 */
	public ExceedLimitException() {
	}

	/**
	 * @param message
	 */
	public ExceedLimitException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ExceedLimitException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ExceedLimitException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ExceedLimitException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
