package com.duowan.gamebox.msgcenter.manager;

import java.util.List;

import com.duowan.gamebox.msgcenter.manager.vo.Invitation;
import com.duowan.gamebox.msgcenter.manager.vo.InvitationDetail;
import com.duowan.gamebox.msgcenter.manager.vo.User;
import com.duowan.gamebox.msgcenter.queue.NotifyInvitationMsgInternal;
import com.duowan.gamebox.msgcenter.queue.NotifyReplyMsgInternal;

public interface InvitationManager {

	public abstract void addInvitation(Invitation invitation);

	public abstract Invitation getInvitationById(String inviteId);

	public abstract Invitation updateInvitationDetail(InvitationDetail detail);

	public abstract void removeInvitationFromCache(Invitation invitation);

	public abstract List<NotifyInvitationMsgInternal> queryUnreadInvitations(User toUser,
			int secondsIn);

	public abstract List<NotifyReplyMsgInternal> queryUnreadReplies(User toUser, int secondsIn);
}