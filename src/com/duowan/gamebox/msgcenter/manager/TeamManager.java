package com.duowan.gamebox.msgcenter.manager;

import java.util.Set;

public interface TeamManager {

	public static int DEFAULT_TIMEOUT = 600;

	/**
	 * @return the timeOut
	 */
	public abstract int getTimeOut();

	/**
	 * @param timeOut
	 *            the timeOut to set
	 */
	public abstract void setTimeOut(int timeOut);

	public abstract void addTokenToTeam(String token, String team);

	public abstract void removeTokenFromTeam(String token, String team);

	public abstract Set<String> getTokens(String team);

	public abstract void removeTeam(String team);

}