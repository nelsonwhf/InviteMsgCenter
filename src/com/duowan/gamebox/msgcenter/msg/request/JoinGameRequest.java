/**
 * 
 */
package com.duowan.gamebox.msgcenter.msg.request;

import com.duowan.gamebox.msgcenter.msg.AbstractMessage;
import com.duowan.gamebox.msgcenter.msg.vo.JoinGameRequestMsg;

/**
 * @author zhangtao.robin
 * 
 */
public class JoinGameRequest extends AbstractMessage<JoinGameRequestMsg> {

	public JoinGameRequest() {
		super();
		setCode(JOIN_GAME_CODE);
	}

}
