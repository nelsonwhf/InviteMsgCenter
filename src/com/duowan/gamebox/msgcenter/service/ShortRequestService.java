/**
 * 
 */
package com.duowan.gamebox.msgcenter.service;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duowan.gamebox.msgcenter.listener.SingletonVars;
import com.duowan.gamebox.msgcenter.manager.ServiceManager;
import com.duowan.gamebox.msgcenter.msg.AbstractMessage;
import com.duowan.gamebox.msgcenter.msg.response.ErrorResponse;
import com.duowan.gamebox.msgcenter.serde.MessageDeserializer;
import com.duowan.gamebox.msgcenter.service.call.ServiceManagerCaller;

/**
 * @author zhangtao.robin
 * 
 */
@Path("/request")
public class ShortRequestService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doShortRequest(ObjectNode jsonNode) {
		MessageDeserializer requestMessageDeserializer = SingletonVars.get()
				.getRequestMessageDeserializer();
		AbstractMessage<?> theMessage = null;
		try {
			theMessage = requestMessageDeserializer.deserialize(jsonNode);
		} catch (IOException e) {
			String s = "Deserialize json failed: " + e;
			log.error(s);
			ErrorResponse errorResponse = ErrorResponse.newErrorRequestResponse(s);
			return genRestfulErrorResponse(errorResponse);
		}
		if (log.isDebugEnabled()) {
			log.debug("Receive request: " + theMessage);
		}

		ServiceManager serviceManager = SingletonVars.get().getServiceManager();
		ServiceManagerCaller serviceManagerCaller = SingletonVars.get().getServiceManagerCaller();
		AbstractMessage<?> result = serviceManagerCaller.callMethod(theMessage, serviceManager);
		int statusCode = 200;
		if (result.getCode() / 100 != 2) {
			statusCode = result.getCode();
		}
		if (log.isInfoEnabled()) {
			log.info("Send response: " + result);
		}
		return Response.status(statusCode).entity(result).build();
	}

	private Response genRestfulErrorResponse(ErrorResponse errorResponse) {
		if (log.isInfoEnabled()) {
			log.info("Send error response: " + errorResponse);
		}
		return Response.status(errorResponse.getCode()).entity(errorResponse).build();
	}

}
