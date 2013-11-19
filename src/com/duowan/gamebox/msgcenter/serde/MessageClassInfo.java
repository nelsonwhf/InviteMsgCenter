package com.duowan.gamebox.msgcenter.serde;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.duowan.gamebox.msgcenter.msg.AbstractMessage;

public class MessageClassInfo {

	private static final String SET_MSG = "setMsg";
	private Class<? extends AbstractMessage<?>> mainClass;
	private Class<?> msgClass;
	private Method setMsgMethod;

	public MessageClassInfo() {
		super();
	}

	public MessageClassInfo(Class<? extends AbstractMessage<?>> mainClass) {
		super();
		setMainClass(mainClass);
	}

	/**
	 * @return the mainClass
	 */
	public Class<? extends AbstractMessage<?>> getMainClass() {
		return mainClass;
	}

	/**
	 * @param mainClass
	 *            the mainClass to set
	 */
	public void setMainClass(Class<? extends AbstractMessage<?>> mainClass) {
		this.mainClass = mainClass;

		try {
			setMsgMethod = this.mainClass.getMethod(SET_MSG, Object.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		ParameterizedType genType = (ParameterizedType) this.mainClass.getGenericSuperclass();
		Type[] params = genType.getActualTypeArguments();
		this.msgClass = (Class<?>) params[0];
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
	 * @return the setMsgMethod
	 */
	public Method getSetMsgMethod() {
		return setMsgMethod;
	}

	/**
	 * @param setMsgMethod
	 *            the setMsgMethod to set
	 */
	public void setSetMsgMethod(Method setMsgMethod) {
		this.setMsgMethod = setMsgMethod;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MessageClassInfo [mainClass=").append(mainClass).append(", msgClass=")
				.append(msgClass).append(", setMsgMethod=").append(setMsgMethod).append("]");
		return builder.toString();
	}

}
