/**
 * 
 */
package com.duowan.gamebox.msgcenter.msg.request;

import com.duowan.gamebox.msgcenter.msg.AbstractMessage;
import com.duowan.gamebox.msgcenter.msg.vo.SendReplyRequestMsg;

/**
 * @author zhangtao.robin
 * 
 */
public class SendReplyRequest extends AbstractMessage<SendReplyRequestMsg> {

	public SendReplyRequest() {
		super();
		setCode(SEND_REPLY_CODE);
	}

}
