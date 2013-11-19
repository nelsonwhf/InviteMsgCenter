/**
 * 
 */
package com.duowan.gamebox.msgcenter.msg.response;

import com.duowan.gamebox.msgcenter.msg.AbstractMessage;
import com.duowan.gamebox.msgcenter.msg.vo.ErrorMsg;

/**
 * @author zhangtao.robin
 * 
 */
public class ErrorResponse extends AbstractMessage<ErrorMsg> {

	public ErrorResponse() {
		super();
	}

	public ErrorResponse(int code) {
		super();
		setCode(code);
	}

	public ErrorResponse(int code, String error) {
		super();
		setCode(code);
		setMsg(new ErrorMsg(error));
	}

	public static ErrorResponse newErrorRequestResponse(String error) {
		return new ErrorResponse(AbstractMessage.ERROR_REQUEST_CODE, error);
	}

	public static ErrorResponse newNonAutResponse(String error) {
		return new ErrorResponse(AbstractMessage.ERROR_NONAUTH_CODE, error);
	}

	public static ErrorResponse newForbiddenResponse(String error) {
		return new ErrorResponse(AbstractMessage.ERROR_FORBIDDEN_CODE, error);
	}

	public static ErrorResponse newInternalErrorResponse(String error) {
		return new ErrorResponse(AbstractMessage.ERROR_INTERNAL_CODE, error);
	}

}
