/**
 * 
 */
package com.duowan.gamebox.msgcenter.msg.response;

import com.duowan.gamebox.msgcenter.msg.AbstractMessage;
import com.duowan.gamebox.msgcenter.msg.vo.SendReplyResponseMsg;

/**
 * @author zhangtao.robin
 * 
 */
public class SendReplyResponse extends AbstractMessage<SendReplyResponseMsg> {

	public SendReplyResponse() {
		super();
		setCode(SEND_REPLY_CODE);
	}

}
