/**
 * 
 */
package com.duowan.gamebox.msgcenter.msg.response;

import com.duowan.gamebox.msgcenter.msg.AbstractMessage;

/**
 * @author zhangtao.robin
 * 
 */
public class NoopResponse extends AbstractMessage<Object> {

	public NoopResponse() {
		super();
		setCode(NOOP_CODE);
	}

}
