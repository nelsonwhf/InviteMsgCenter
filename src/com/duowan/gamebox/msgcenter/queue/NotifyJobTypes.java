package com.duowan.gamebox.msgcenter.queue;

import java.util.HashMap;
import java.util.Map;

public class NotifyJobTypes {

	public static final String NotifyCloseConnection = "NotifyCloseConnection";
	public static final String NotifyInvitation = "NotifyInvitation";
	public static final String NotifyReply = "NotifyReply";
	public static final String NotifyJoinGame = "NotifyJoinGame";

	private Map<String, Class<?>> mapJobTypes = new HashMap<String, Class<?>>();

	private NotifyJobTypes() {
		mapJobTypes.put(NotifyCloseConnection, NotifyCloseConnection.class);
		mapJobTypes.put(NotifyInvitation, NotifyInvitation.class);
		mapJobTypes.put(NotifyReply, NotifyReply.class);
		mapJobTypes.put(NotifyJoinGame, NotifyJoinGame.class);
	}

	/**
	 * @return the mapJobTypes
	 */
	public Map<String, Class<?>> getMapJobTypes() {
		return mapJobTypes;
	}

	private static final NotifyJobTypes notifyJobTypes = new NotifyJobTypes();

	public static NotifyJobTypes get() {
		return notifyJobTypes;
	}
}
