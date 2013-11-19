/**
 * 
 */
package com.duowan.gamebox.msgcenter.manager.exception;

/**
 * @author zhangtao.robin
 * 
 */
public class InvalidYyTokenException extends Exception {

	private static final long serialVersionUID = 8641275379271647066L;

	public InvalidYyTokenException() {
		super();
	}

	public InvalidYyTokenException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidYyTokenException(String message) {
		super(message);
	}

	public InvalidYyTokenException(Throwable cause) {
		super(cause);
	}

	public InvalidYyTokenException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
