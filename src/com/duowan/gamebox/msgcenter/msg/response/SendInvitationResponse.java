/**
 * 
 */
package com.duowan.gamebox.msgcenter.msg.response;

import com.duowan.gamebox.msgcenter.msg.AbstractMessage;
import com.duowan.gamebox.msgcenter.msg.vo.SendInvitationResponseMsg;

/**
 * @author zhangtao.robin
 * 
 */
public class SendInvitationResponse extends AbstractMessage<SendInvitationResponseMsg> {

	public SendInvitationResponse() {
		super();
		setCode(SEND_INVITATION_CODE);
	}

}
