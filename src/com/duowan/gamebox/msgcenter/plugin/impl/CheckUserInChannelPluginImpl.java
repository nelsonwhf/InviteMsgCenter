/**
 * 
 */
package com.duowan.gamebox.msgcenter.plugin.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

import com.duowan.common.loadbalance.FailOverProcessor;
import com.duowan.common.loadbalance.ProcessException;
import com.duowan.gamebox.msgcenter.manager.exception.InvalidRequestException;
import com.duowan.gamebox.msgcenter.manager.exception.InvalidRequestWithCodeException;
import com.duowan.gamebox.msgcenter.manager.exception.ServiceErrorException;
import com.duowan.gamebox.msgcenter.manager.vo.User;
import com.duowan.gamebox.msgcenter.msg.request.SendInvitationRequest;
import com.duowan.gamebox.msgcenter.plugin.PreSendInvitationPlugin;
import com.duowan.gamebox.msgcenter.redis.NameSpace;
import com.duowan.gamebox.msgcenter.redis.RedisUtils;

/**
 * @author zhangtao.robin
 * 
 */
public class CheckUserInChannelPluginImpl implements PreSendInvitationPlugin {

	public static final int REJECTED_ERROR_CODE = 420;

	private static final String PASSED = "1";
	private static final String REJECTED = "0";
	private static final String SQL_SELECT_WHITELIST = "SELECT shortid FROM channel_white_list_short";
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private FailOverProcessor<String, String> failOverProcessor;
	private Pool<Jedis> jedisPool;
	private DataSource dataSource;
	private boolean debugMode = false;

	private int chPassedTimeout = 30;
	private int whiteListTimeout = 600;

	private Object lock = new Object();

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.plugin.PreSendInvitationPlugin#preSendInvitation(com.duowan.gamebox.msgcenter.manager.vo.User, java.util.List, com.duowan.gamebox.msgcenter.msg.request.SendInvitationRequest)
	 */
	@Override
	public void preSendInvitation(User fromUser, List<User> toUserList,
			SendInvitationRequest request) throws InvalidRequestException, ServiceErrorException {
		if (debugMode) {
			return;
		}

		String tid = request.getMsg().getTid();
		if (tid == null) {
			throw new InvalidRequestWithCodeException(REJECTED_ERROR_CODE, "tid is null");
		}
		// get pass / reject from cache by tid
		Boolean passed = getChPassedFromCache(tid);
		if (passed == null) {
			// not decided
			// get short id
			String shortId = null;
			try {
				shortId = failOverProcessor.process(tid);
			} catch (ProcessException e) {
				Throwable cause = e.getCause();
				if (cause != null) {
					throw new InvalidRequestWithCodeException(REJECTED_ERROR_CODE,
							"Rejected because of tid: " + cause, cause);
				} else {
					throw new InvalidRequestWithCodeException(REJECTED_ERROR_CODE,
							"Rejected because of tid: " + e, e);
				}
			}

			// check short id
			passed = checkShortId(shortId, tid);
		}

		// set pass / reject to cache by tid
		setChPassedFromCache(tid, passed);

		if (passed) {
			// passed
			return;
		}
		// rejected
		throw new InvalidRequestWithCodeException(REJECTED_ERROR_CODE, "Rejected because of tid: "
				+ tid);
	}

	private Boolean getChPassedFromCache(String tid) {
		Jedis jedis = jedisPool.getResource();
		try {
			String passedStr = jedis.get(RedisUtils.createKey(NameSpace.CHANNEL_PASSED_KEY, tid));
			if (passedStr == null) {
				// not decided
				return null;
			}
			if (PASSED.equals(passedStr)) {
				// passed
				return Boolean.TRUE;
			}
			// rejected
			return Boolean.FALSE;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	private void setChPassedFromCache(String tid, boolean passed) {
		String key = RedisUtils.createKey(NameSpace.CHANNEL_PASSED_KEY, tid);
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.set(key, passed ? PASSED : REJECTED);
			jedis.expire(key, chPassedTimeout);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	private Boolean checkShortId(String shortId, String tid) {
		// check short id: If( length(short id)>4), PASS;
		if (shortId.length() > 4) {
			return Boolean.TRUE;
		}

		// else if (shor id in White List), PASS; else FAILED.
		String key = NameSpace.WHITELIST_SHORT_KEY;
		Jedis jedis = jedisPool.getResource();
		try {
			// check short id in cache
			if (jedis.exists(key)) {
				return jedis.sismember(key, shortId);
			}
			// load list from DB
			synchronized (lock) {
				if (!jedis.exists(key)) {
					List<String> ids = loadWhiteList();
					if (ids != null && ids.size() != 0) {
						// set list to cache
						jedis.sadd(key, ids.toArray(new String[ids.size()]));
						jedis.expire(key, whiteListTimeout);
					}
				}
			}
			// check exist
			return jedis.sismember(key, shortId);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	private List<String> loadWhiteList() {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rst = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(SQL_SELECT_WHITELIST);
			rst = stmt.executeQuery();
			List<String> ids = null;
			while (rst.next()) {
				if (ids == null) {
					ids = new ArrayList<String>();
				}
				ids.add(rst.getString(1));
			}
			log.info("loadWhiteList success, number elements: " + (ids == null ? 0 : ids
					.size()));
			return ids;
		} catch (SQLException e) {
			log.error("loadWhiteList failed: " + e, e);
			return null;
		} finally {
			closeResultSet(rst);
			closeStatement(stmt);
			closeConn(conn);
		}
	}

	private void closeResultSet(ResultSet rst) {
		if (rst != null) {
			try {
				rst.close();
			} catch (SQLException e) {
			}
		}
	}

	private void closeStatement(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}

	private void closeConn(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * @return the failOverProcessor
	 */
	public FailOverProcessor<String, String> getFailOverProcessor() {
		return failOverProcessor;
	}

	/**
	 * @param failOverProcessor
	 *            the failOverProcessor to set
	 */
	public void setFailOverProcessor(FailOverProcessor<String, String> failOverProcessor) {
		this.failOverProcessor = failOverProcessor;
	}

	/**
	 * @return the jedisPool
	 */
	public Pool<Jedis> getJedisPool() {
		return jedisPool;
	}

	/**
	 * @param jedisPool
	 *            the jedisPool to set
	 */
	public void setJedisPool(Pool<Jedis> jedisPool) {
		this.jedisPool = jedisPool;
	}

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @return the chPassedTimeout
	 */
	public int getChPassedTimeout() {
		return chPassedTimeout;
	}

	/**
	 * @param chPassedTimeout
	 *            the chPassedTimeout to set
	 */
	public void setChPassedTimeout(int chPassedTimeout) {
		this.chPassedTimeout = chPassedTimeout;
	}

	/**
	 * @return the whiteListTimeout
	 */
	public int getWhiteListTimeout() {
		return whiteListTimeout;
	}

	/**
	 * @param whiteListTimeout
	 *            the whiteListTimeout to set
	 */
	public void setWhiteListTimeout(int whiteListTimeout) {
		this.whiteListTimeout = whiteListTimeout;
	}

	/**
	 * @return the debugMode
	 */
	public boolean isDebugMode() {
		return debugMode;
	}

	/**
	 * @param debugMode
	 *            the debugMode to set
	 */
	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}

}
