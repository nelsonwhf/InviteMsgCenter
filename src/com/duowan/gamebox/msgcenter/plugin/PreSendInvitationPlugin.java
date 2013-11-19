/**
 * 
 */
package com.duowan.gamebox.msgcenter.plugin;

import java.util.List;

import com.duowan.gamebox.msgcenter.manager.exception.InvalidRequestException;
import com.duowan.gamebox.msgcenter.manager.exception.ServiceErrorException;
import com.duowan.gamebox.msgcenter.manager.vo.User;
import com.duowan.gamebox.msgcenter.msg.request.SendInvitationRequest;

/**
 * @author zhangtao.robin
 * 
 */
public interface PreSendInvitationPlugin {

	public void preSendInvitation(User fromUser, List<User> toUserList,
			SendInvitationRequest request)
			throws InvalidRequestException, ServiceErrorException;

}
