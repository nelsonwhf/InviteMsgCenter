/**
 * 
 */
package com.duowan.gamebox.msgcenter.msg.response;

import com.duowan.gamebox.msgcenter.msg.AbstractMessage;
import com.duowan.gamebox.msgcenter.msg.vo.NotifyInvitationMsg;

/**
 * @author zhangtao.robin
 * 
 */
public class NotifyInvitationResponse extends AbstractMessage<NotifyInvitationMsg> {

	public NotifyInvitationResponse() {
		super();
		setCode(NOTIFY_INVITATION_CODE);
	}

}
