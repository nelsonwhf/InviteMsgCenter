/**
 * 
 */
package com.duowan.gamebox.msgcenter.service.call;

import java.lang.reflect.Method;

import com.duowan.gamebox.msgcenter.manager.ServiceManager;

/**
 * @author zhangtao.robin
 * 
 */
public class CallerMethodInfo {
	private Class<?> msgClass;
	private Method callMethod;
	private String callName;

	public CallerMethodInfo(String callName, Class<?> msgClass) {
		setCallName(callName);
		setMsgClass(msgClass);

		try {
			setCallMethod(ServiceManager.class.getMethod(callName, msgClass));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the msgClass
	 */
	public Class<?> getMsgClass() {
		return msgClass;
	}

	/**
	 * @param msgClass
	 *            the msgClass to set
	 */
	public void setMsgClass(Class<?> msgClass) {
		this.msgClass = msgClass;
	}

	/**
	 * @return the callMethod
	 */
	public Method getCallMethod() {
		return callMethod;
	}

	/**
	 * @param callMethod
	 *            the callMethod to set
	 */
	public void setCallMethod(Method callMethod) {
		this.callMethod = callMethod;
	}

	/**
	 * @return the callName
	 */
	public String getCallName() {
		return callName;
	}

	/**
	 * @param callName
	 *            the callName to set
	 */
	public void setCallName(String callName) {
		this.callName = callName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CallerInfo [msgClass=").append(msgClass).append(", callMethod=")
				.append(callMethod).append(", callName=").append(callName).append("]");
		return builder.toString();
	}
}
