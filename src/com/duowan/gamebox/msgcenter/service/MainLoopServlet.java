/**
 * 
 */
package com.duowan.gamebox.msgcenter.service;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duowan.gamebox.msgcenter.listener.SingletonVars;
import com.duowan.gamebox.msgcenter.manager.ServiceManager;
import com.duowan.gamebox.msgcenter.manager.exception.AuthorizeErrorException;
import com.duowan.gamebox.msgcenter.manager.exception.InvalidRequestException;
import com.duowan.gamebox.msgcenter.manager.exception.ServiceErrorException;
import com.duowan.gamebox.msgcenter.manager.vo.User;
import com.duowan.gamebox.msgcenter.msg.AbstractMessage;
import com.duowan.gamebox.msgcenter.msg.request.LoginRequest;
import com.duowan.gamebox.msgcenter.msg.response.ErrorResponse;
import com.duowan.gamebox.msgcenter.msg.response.LoginResponse;
import com.duowan.gamebox.msgcenter.serde.MessageDeserializer;
import com.duowan.gamebox.msgcenter.serde.MessageSerializer;

/**
 * @author zhangtao.robin
 * 
 */
@WebServlet(asyncSupported = true, name = "MainLoopServlet", urlPatterns = "/loop")
public class MainLoopServlet extends HttpServlet {

	private static final String POST = "POST";

	private static final String UTF_8 = "UTF-8";

	private static final String APPLICATION_JSON = "application/json";

	private static final long serialVersionUID = -5586682923797196852L;

	private MessageDeserializer requestMessageDeserializer;
	private MessageSerializer messageSerializer;
	private ServiceManager serviceManager;
	private MainLoopAsyncListener mainLoopAsyncListener;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		requestMessageDeserializer = SingletonVars.get().getRequestMessageDeserializer();
		messageSerializer = SingletonVars.get().getMessageSerializer();
		serviceManager = SingletonVars.get().getServiceManager();
		mainLoopAsyncListener = new MainLoopAsyncListener();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// check content type
		String contentType = request.getContentType();
		if (contentType == null || contentType.indexOf("json") == -1) {
			String s = "Invalid content type: " + contentType;
			log.error(s);
			ErrorResponse errorResponse = ErrorResponse.newErrorRequestResponse(s);
			sendErrorResponse(errorResponse, response);
			return;
		}

		// request Message Deserializer
		AbstractMessage<?> theMessage = null;
		try {
			theMessage = requestMessageDeserializer.deserialize(request.getInputStream());
		} catch (IOException e) {
			String s = "Deserialize json failed: " + e;
			log.error(s);
			ErrorResponse errorResponse = ErrorResponse.newErrorRequestResponse(s);
			sendErrorResponse(errorResponse, response);
			return;
		}
		if (theMessage.getCode() != AbstractMessage.LOGIN_CODE) {
			String s = "Invalid code: " + theMessage.getCode();
			log.error(s);
			ErrorResponse errorResponse = ErrorResponse.newErrorRequestResponse(s);
			sendErrorResponse(errorResponse, response);
			return;
		}

		// asnyc start
		AsyncContext asyncContext = request.startAsync();
		asyncContext.setTimeout(0L);
		asyncContext.addListener(mainLoopAsyncListener);
		if (log.isDebugEnabled()) {
			log.debug("Start async: " + asyncContext);
		}
		// call service manager: login
		LoginRequest loginRequest = (LoginRequest) theMessage;
		LoginResponse loginResponse = null;
		try {
			loginResponse = serviceManager.login(loginRequest, asyncContext);
		} catch (ServiceErrorException e) {
			String s = "Call login() failed: " + e;
			log.error(s);
			ErrorResponse errorResponse = ErrorResponse.newInternalErrorResponse(s);
			sendErrorResponse(errorResponse, response);
			asyncContext.complete();
			return;
		} catch (AuthorizeErrorException e) {
			log.error("Call login() failed: " + e);
			ErrorResponse errorResponse = ErrorResponse.newNonAutResponse(e.toString());
			sendErrorResponse(errorResponse, response);
			asyncContext.complete();
			return;
		} catch (InvalidRequestException e) {
			log.error("Call login() failed: " + e);
			ErrorResponse errorResponse = ErrorResponse.newErrorRequestResponse(e.getMessage());
			sendErrorResponse(errorResponse, response);
			asyncContext.complete();
			return;
		}
		if (log.isInfoEnabled()) {
			log.info("Call login() success: " + loginResponse);
		}
		response.setStatus(200);
		messageSerializer.serializeWithLineBreak(response.getOutputStream(), loginResponse);

		// query and send unread messages
		User user = loginResponse.getMsg().getUser();
		serviceManager.sendUnreadInvitations(user);
		serviceManager.sendUnreadReplies(user);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(APPLICATION_JSON);
		response.setCharacterEncoding(UTF_8);

		String method = request.getMethod();
		if (method.equals(POST)) {
			doPost(request, response);
		} else {
			ErrorResponse errorResponse = ErrorResponse.newForbiddenResponse("Unsupported method "
					+ method);
			sendErrorResponse(errorResponse, response);
			response.getOutputStream().close();
			return;
		}
	}

	private void sendErrorResponse(ErrorResponse errorResponse, HttpServletResponse response)
			throws IOException {
		if (log.isInfoEnabled()) {
			log.info("Send error response: " + errorResponse);
		}
		response.setStatus(errorResponse.getCode());
		messageSerializer.serialize(response.getOutputStream(), errorResponse);
		response.getOutputStream().close();
	}

	class MainLoopAsyncListener implements AsyncListener {

		@Override
		public void onComplete(AsyncEvent asyncEvent) throws IOException {
			log.info("onComplete " + asyncEvent.getAsyncContext());
			serviceManager.logout(asyncEvent.getAsyncContext());
		}

		@Override
		public void onTimeout(AsyncEvent asyncEvent) throws IOException {
			// should not happen
			log.info("onTimeout " + asyncEvent.getAsyncContext());
			asyncEvent.getAsyncContext().complete();
		}

		@Override
		public void onError(AsyncEvent asyncEvent) throws IOException {
			log.info("onError " + asyncEvent.getAsyncContext());
			asyncEvent.getAsyncContext().complete();
		}

		@Override
		public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
			log.info("onStartAsync " + asyncEvent.getAsyncContext());
		}

	}
}
