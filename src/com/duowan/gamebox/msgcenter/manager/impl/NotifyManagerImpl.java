/**
 * 
 */
package com.duowan.gamebox.msgcenter.manager.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.greghaines.jesque.Config;
import net.greghaines.jesque.worker.WorkerImplFactory;
import net.greghaines.jesque.worker.WorkerPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duowan.gamebox.msgcenter.manager.NotifyManager;
import com.duowan.gamebox.msgcenter.queue.NotifyJobTypes;

/**
 * @author zhangtao.robin
 * 
 */
public class NotifyManagerImpl implements NotifyManager {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private String notifyQueue;
	private Config jesqueConfig;
	private Map<String, Class<?>> mapJobTypes = NotifyJobTypes.get().getMapJobTypes();
	private int threadsNum = 3;

	private WorkerImplFactory workerImplFactory;
	private WorkerPool workerPool;
	private Thread theThread;

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.NotiyManager#getNotifyQueue()
	 */
	@Override
	public String getNotifyQueue() {
		return notifyQueue;
	}

	/**
	 * @param notifyQueue
	 *            the notifyQueue to set
	 */
	public void setNotifyQueue(String notifyQueue) {
		this.notifyQueue = notifyQueue;
	}

	/**
	 * @return the jesqueConfig
	 */
	public Config getJesqueConfig() {
		return jesqueConfig;
	}

	/**
	 * @param jesqueConfig
	 *            the jesqueConfig to set
	 */
	public void setJesqueConfig(Config jesqueConfig) {
		this.jesqueConfig = jesqueConfig;
	}

	/**
	 * @return the threadsNum
	 */
	public int getThreadsNum() {
		return threadsNum;
	}

	/**
	 * @param threadsNum
	 *            the threadsNum to set
	 */
	public void setThreadsNum(int threadsNum) {
		this.threadsNum = threadsNum;
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.NotiyManager#startNotifyWorker()
	 */
	@Override
	public void startNotifyWorker() {
		List<String> queues = Arrays.asList(notifyQueue);
		workerImplFactory = new WorkerImplFactory(jesqueConfig, queues, mapJobTypes);
		workerPool = new WorkerPool(workerImplFactory, threadsNum);
		theThread = new Thread(workerPool);
		theThread.start();
		log.info("Start notify worker: " + queues);
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.NotiyManager#stopNotifyWorker()
	 */
	@Override
	public void stopNotifyWorker() {
		if (workerPool != null) {
			try {
				workerPool.endAndJoin(true, 1000L);
			} catch (InterruptedException e) {
			}
		}
		if (theThread != null) {
			theThread.interrupt();
			try {
				theThread.join(1000L);
			} catch (InterruptedException e) {
			}
		}
		log.info("Stop notify worker");
	}
}
