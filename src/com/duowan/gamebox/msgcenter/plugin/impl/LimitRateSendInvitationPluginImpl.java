package com.duowan.gamebox.msgcenter.plugin.impl;

import java.util.List;

import com.duowan.gamebox.msgcenter.manager.exception.ExceedLimitException;
import com.duowan.gamebox.msgcenter.manager.exception.InvalidRequestException;
import com.duowan.gamebox.msgcenter.manager.exception.InvalidRequestWithCodeException;
import com.duowan.gamebox.msgcenter.manager.exception.ServiceErrorException;
import com.duowan.gamebox.msgcenter.manager.vo.User;
import com.duowan.gamebox.msgcenter.msg.request.SendInvitationRequest;
import com.duowan.gamebox.msgcenter.plugin.PreSendInvitationPlugin;
import com.duowan.gamebox.msgcenter.rate.RateLimitManager;
import com.duowan.gamebox.msgcenter.redis.NameSpace;
import com.duowan.gamebox.msgcenter.redis.RedisUtils;

public class LimitRateSendInvitationPluginImpl implements PreSendInvitationPlugin {

	public static final int LIMITED_ERROR_CODE = 421;

	private RateLimitManager inviteLimitManager;

	@Override
	public void preSendInvitation(User fromUser, List<User> toUserList,
			SendInvitationRequest request) throws InvalidRequestException, ServiceErrorException {
		// Limit call rate
		String controlKey = RedisUtils.createKey(NameSpace.SEND_RATE_KEY, fromUser.getUserId(),
				fromUser.getUserType());
		try {
			inviteLimitManager.limitCall(controlKey);
		} catch (ExceedLimitException e) {
			throw new InvalidRequestWithCodeException(LIMITED_ERROR_CODE, e);
		}
	}

	/**
	 * @return the inviteLimitManager
	 */
	public RateLimitManager getInviteLimitManager() {
		return inviteLimitManager;
	}

	/**
	 * @param inviteLimitManager
	 *            the inviteLimitManager to set
	 */
	public void setInviteLimitManager(RateLimitManager inviteLimitManager) {
		this.inviteLimitManager = inviteLimitManager;
	}

}
