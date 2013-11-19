package com.duowan.gamebox.msgcenter.service.call;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duowan.gamebox.msgcenter.manager.ServiceManager;
import com.duowan.gamebox.msgcenter.manager.exception.InvalidRequestException;
import com.duowan.gamebox.msgcenter.manager.exception.InvalidRequestWithCodeException;
import com.duowan.gamebox.msgcenter.manager.exception.ServiceErrorException;
import com.duowan.gamebox.msgcenter.msg.AbstractMessage;
import com.duowan.gamebox.msgcenter.msg.response.ErrorResponse;

public class ServiceManagerCaller {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private Map<Integer, CallerMethodInfo> codeMapping = new HashMap<Integer, CallerMethodInfo>();

	public AbstractMessage<?> callMethod(AbstractMessage<?> msg, ServiceManager serviceManager) {
		int code = msg.getCode();
		CallerMethodInfo info = codeMapping.get(code);
		if (info == null) {
			log.error("Cannot find mapping by code=" + code
					+ ", return invalid response, with request: "
					+ msg);
			return ErrorResponse.newErrorRequestResponse("Invalid code: " + code);
		}
		if (!msg.getClass().equals(info.getMsgClass())) {
			log.error("Message class should be " + info.getMsgClass() + ", but " + msg.getClass()
					+ ", with request: " + msg);
			return ErrorResponse.newInternalErrorResponse("Message class should be "
					+ info.getMsgClass() + ", but " + msg.getClass());
		}

		if (log.isInfoEnabled()) {
			log.info("Invoke " + info.getCallName() + "() with request: " + msg);
		}
		try {
			Object result = info.getCallMethod().invoke(serviceManager, msg);
			if (AbstractMessage.class.isAssignableFrom(result.getClass())) {
				return (AbstractMessage<?>) result;
			} else {
				String s = "Invoke " + info.getCallName()
						+ "() should return class extends of AbstractMessage, but "
						+ result.getClass();
				log.error(s + ", with request: " + msg);
				return ErrorResponse.newInternalErrorResponse(s);
			}
		} catch (IllegalAccessException e) {
			String s = "Invoke " + info.getCallName() + "() failed: " + e;
			log.error(s + ", with request: " + msg, e);
			return ErrorResponse.newInternalErrorResponse(s);
		} catch (IllegalArgumentException e) {
			String s = "Invoke " + info.getCallName() + "() failed: " + e;
			log.error(s + ", with request: " + msg, e);
			return ErrorResponse.newInternalErrorResponse(s);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			String s = "Invoke " + info.getCallName() + "() failed: " + cause;
			log.error(s + ", with request: " + msg, cause);
			if (InvalidRequestException.class.isAssignableFrom(cause.getClass())) {
				if (InvalidRequestWithCodeException.class.isAssignableFrom(cause.getClass())) {
					InvalidRequestWithCodeException irwce = (InvalidRequestWithCodeException) cause;
					return new ErrorResponse(irwce.getCode(), cause.getMessage());
				}
				return ErrorResponse.newErrorRequestResponse(cause.getMessage());
			}
			if (ServiceErrorException.class.isAssignableFrom(cause.getClass())) {
				return ErrorResponse.newInternalErrorResponse(cause.getMessage());
			}
			return ErrorResponse.newInternalErrorResponse(cause.getMessage());
		}
	}

	public void addCodeMapping(int code, String callName, Class<?> msgClass) {
		CallerMethodInfo info = new CallerMethodInfo(callName, msgClass);
		codeMapping.put(code, info);
		if (log.isInfoEnabled()) {
			log.info("Add code=" + code + ", mapping=" + info);
		}
	}

	public void addCodeMapping(AbstractMessage<?> msg, String callName) {
		this.addCodeMapping(msg.getCode(), callName, msg.getClass());
	}
}
