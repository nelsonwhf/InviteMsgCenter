package com.duowan.gamebox.msgcenter.manager;

public interface NotifyManager {

	/**
	 * @return the notifyQueue
	 */
	public abstract String getNotifyQueue();

	public abstract void startNotifyWorker();

	public abstract void stopNotifyWorker();

}