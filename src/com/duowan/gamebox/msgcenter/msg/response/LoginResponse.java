/**
 * 
 */
package com.duowan.gamebox.msgcenter.msg.response;

import com.duowan.gamebox.msgcenter.msg.AbstractMessage;
import com.duowan.gamebox.msgcenter.msg.vo.LoginResponseMsg;

/**
 * @author zhangtao.robin
 * 
 */
public class LoginResponse extends AbstractMessage<LoginResponseMsg> {

	public LoginResponse() {
		super();
		setCode(LOGIN_CODE);
	}

}
