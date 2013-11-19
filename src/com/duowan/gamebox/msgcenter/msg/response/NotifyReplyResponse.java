/**
 * 
 */
package com.duowan.gamebox.msgcenter.msg.response;

import com.duowan.gamebox.msgcenter.msg.AbstractMessage;
import com.duowan.gamebox.msgcenter.msg.vo.NotifyReplyMsg;

/**
 * @author zhangtao.robin
 * 
 */
public class NotifyReplyResponse extends AbstractMessage<NotifyReplyMsg> {

	public NotifyReplyResponse() {
		super();
		setCode(NOTIFY_REPLY_CODE);
	}

}
