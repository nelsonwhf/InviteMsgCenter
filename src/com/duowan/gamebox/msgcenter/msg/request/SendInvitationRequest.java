/**
 * 
 */
package com.duowan.gamebox.msgcenter.msg.request;

import com.duowan.gamebox.msgcenter.msg.AbstractMessage;
import com.duowan.gamebox.msgcenter.msg.vo.SendInvitationRequestMsg;

/**
 * @author zhangtao.robin
 * 
 */
public class SendInvitationRequest extends AbstractMessage<SendInvitationRequestMsg> {

	public SendInvitationRequest() {
		super();
		setCode(SEND_INVITATION_CODE);
	}

}
