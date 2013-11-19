/**
 * 
 */
package com.duowan.gamebox.msgcenter.msg.response;

import com.duowan.gamebox.msgcenter.msg.AbstractMessage;
import com.duowan.gamebox.msgcenter.msg.vo.ServerListMsg;

/**
 * @author zhangtao.robin
 * 
 */
public class ServerListResponse extends AbstractMessage<ServerListMsg> {

	public ServerListResponse() {
		super();
		setCode(SERVER_LIST_CODE);
	}

}
