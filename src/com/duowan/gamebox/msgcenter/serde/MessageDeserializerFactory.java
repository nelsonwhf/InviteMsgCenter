package com.duowan.gamebox.msgcenter.serde;

import com.duowan.gamebox.msgcenter.msg.AbstractMessage;
import com.duowan.gamebox.msgcenter.msg.request.JoinGameRequest;
import com.duowan.gamebox.msgcenter.msg.request.LoginRequest;
import com.duowan.gamebox.msgcenter.msg.request.QueryInvitationsRequest;
import com.duowan.gamebox.msgcenter.msg.request.QueryPartnersRequest;
import com.duowan.gamebox.msgcenter.msg.request.SendInvitationRequest;
import com.duowan.gamebox.msgcenter.msg.request.SendReplyRequest;
import com.duowan.gamebox.msgcenter.msg.response.ErrorResponse;
import com.duowan.gamebox.msgcenter.msg.response.JoinGameResponse;
import com.duowan.gamebox.msgcenter.msg.response.LoginResponse;
import com.duowan.gamebox.msgcenter.msg.response.NoopResponse;
import com.duowan.gamebox.msgcenter.msg.response.NotifyInvitationResponse;
import com.duowan.gamebox.msgcenter.msg.response.NotifyReplyResponse;
import com.duowan.gamebox.msgcenter.msg.response.QueryInvitationsResponse;
import com.duowan.gamebox.msgcenter.msg.response.QueryPartnersResponse;
import com.duowan.gamebox.msgcenter.msg.response.SendInvitationResponse;
import com.duowan.gamebox.msgcenter.msg.response.SendReplyResponse;

public class MessageDeserializerFactory {

	private MessageDeserializer requestMessageDeserializer;
	private MessageDeserializer responseMessageDeserializer;

	private MessageDeserializerFactory() {
		requestMessageDeserializer = new MessageDeserializer();
		requestMessageDeserializer.addCodeMapping(new JoinGameRequest());
		requestMessageDeserializer.addCodeMapping(new LoginRequest());
		requestMessageDeserializer.addCodeMapping(new QueryInvitationsRequest());
		requestMessageDeserializer.addCodeMapping(new QueryPartnersRequest());
		requestMessageDeserializer.addCodeMapping(new SendInvitationRequest());
		requestMessageDeserializer.addCodeMapping(new SendReplyRequest());

		responseMessageDeserializer = new MessageDeserializer();
		responseMessageDeserializer.addCodeMapping(new JoinGameResponse());
		responseMessageDeserializer.addCodeMapping(new LoginResponse());
		responseMessageDeserializer.addCodeMapping(new NoopResponse());
		responseMessageDeserializer.addCodeMapping(new NotifyInvitationResponse());
		responseMessageDeserializer.addCodeMapping(new NotifyReplyResponse());
		responseMessageDeserializer.addCodeMapping(new NotifyReplyResponse());
		responseMessageDeserializer.addCodeMapping(new QueryInvitationsResponse());
		responseMessageDeserializer.addCodeMapping(new QueryPartnersResponse());
		responseMessageDeserializer.addCodeMapping(new SendInvitationResponse());
		responseMessageDeserializer.addCodeMapping(new SendReplyResponse());
		responseMessageDeserializer.addCodeMapping(AbstractMessage.ERROR_FORBIDDEN_CODE,
				ErrorResponse.class);
		responseMessageDeserializer.addCodeMapping(AbstractMessage.ERROR_INTERNAL_CODE,
				ErrorResponse.class);
		responseMessageDeserializer.addCodeMapping(AbstractMessage.ERROR_NONAUTH_CODE,
				ErrorResponse.class);
		responseMessageDeserializer.addCodeMapping(AbstractMessage.ERROR_REQUEST_CODE,
				ErrorResponse.class);
	}

	/**
	 * @return the requestMessageDeserializer
	 */
	public MessageDeserializer getRequestMessageDeserializer() {
		return requestMessageDeserializer;
	}

	/**
	 * @return the responseMessageDeserializer
	 */
	public MessageDeserializer getResponseMessageDeserializer() {
		return responseMessageDeserializer;
	}

	private static final MessageDeserializerFactory factory = new MessageDeserializerFactory();

	public static final MessageDeserializerFactory get() {
		return factory;
	}

}
