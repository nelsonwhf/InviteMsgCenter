package com.duowan.gamebox.msgcenter.service.call;

import com.duowan.gamebox.msgcenter.msg.request.JoinGameRequest;
import com.duowan.gamebox.msgcenter.msg.request.QueryInvitationsRequest;
import com.duowan.gamebox.msgcenter.msg.request.QueryPartnersRequest;
import com.duowan.gamebox.msgcenter.msg.request.SendInvitationRequest;
import com.duowan.gamebox.msgcenter.msg.request.SendReplyRequest;

public class ServiceManagerCallerFactory {

	private ServiceManagerCaller serviceManagerCaller;

	private ServiceManagerCallerFactory() {
		serviceManagerCaller = new ServiceManagerCaller();
		serviceManagerCaller.addCodeMapping(new JoinGameRequest(), "joinGame");
		serviceManagerCaller.addCodeMapping(new QueryInvitationsRequest(), "queryInvitations");
		serviceManagerCaller.addCodeMapping(new QueryPartnersRequest(), "queryPartners");
		serviceManagerCaller.addCodeMapping(new SendInvitationRequest(), "sendInvitation");
		serviceManagerCaller.addCodeMapping(new SendReplyRequest(), "sendReply");
	}

	/**
	 * @return the serviceManagerCaller
	 */
	public ServiceManagerCaller getServiceManagerCaller() {
		return serviceManagerCaller;
	}

	/**
	 * @param serviceManagerCaller
	 *            the serviceManagerCaller to set
	 */
	public void setServiceManagerCaller(ServiceManagerCaller serviceManagerCaller) {
		this.serviceManagerCaller = serviceManagerCaller;
	}

	private static final ServiceManagerCallerFactory factory = new ServiceManagerCallerFactory();

	public static final ServiceManagerCallerFactory get() {
		return factory;
	}

}
