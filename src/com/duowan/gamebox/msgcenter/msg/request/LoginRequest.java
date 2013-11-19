/**
 * 
 */
package com.duowan.gamebox.msgcenter.msg.request;

import com.duowan.gamebox.msgcenter.msg.AbstractMessage;
import com.duowan.gamebox.msgcenter.msg.vo.LoginRequestMsg;

/**
 * @author zhangtao.robin
 * 
 */
public class LoginRequest extends AbstractMessage<LoginRequestMsg> {

	public LoginRequest() {
		super();
		setCode(LOGIN_CODE);
	}

}
