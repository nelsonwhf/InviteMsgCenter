/**
 * 
 */
package com.duowan.gamebox.msgcenter.msg.response;

import com.duowan.gamebox.msgcenter.msg.AbstractMessage;

/**
 * @author zhangtao.robin
 * 
 */
public class JoinGameResponse extends AbstractMessage<Object> {

	public JoinGameResponse() {
		super();
		setCode(JOIN_GAME_CODE);
	}

}
