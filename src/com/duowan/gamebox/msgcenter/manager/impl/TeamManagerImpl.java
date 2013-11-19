package com.duowan.gamebox.msgcenter.manager.impl;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

import com.duowan.gamebox.msgcenter.manager.TeamManager;
import com.duowan.gamebox.msgcenter.redis.NameSpace;
import com.duowan.gamebox.msgcenter.redis.RedisUtils;

public class TeamManagerImpl implements TeamManager {

	private Pool<Jedis> jedisPool;

	private int timeOut = TeamManager.DEFAULT_TIMEOUT;

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

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.TeamManager#getTimeOut()
	 */
	@Override
	public int getTimeOut() {
		return timeOut;
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.TeamManager#setTimeOut(int)
	 */
	@Override
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.TeamManager#addTokenToTeam(java.lang.String, java.lang.String)
	 */
	@Override
	public void addTokenToTeam(String token, String team) {
		String teamKey = RedisUtils.createKey(NameSpace.TEAM_MAPTO_TOKENS, team);
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.sadd(teamKey, token);
			jedis.expire(teamKey, timeOut);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.TeamManager#removeTokenFromTeam(java.lang.String, java.lang.String)
	 */
	@Override
	public void removeTokenFromTeam(String token, String team) {
		String teamKey = RedisUtils.createKey(NameSpace.TEAM_MAPTO_TOKENS, team);
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.srem(teamKey, token);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.TeamManager#getTokens(java.lang.String)
	 */
	@Override
	public Set<String> getTokens(String team) {
		String teamKey = RedisUtils.createKey(NameSpace.TEAM_MAPTO_TOKENS, team);
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.smembers(teamKey);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.TeamManager#removeTeam(java.lang.String)
	 */
	@Override
	public void removeTeam(String team) {
		String teamKey = RedisUtils.createKey(NameSpace.TEAM_MAPTO_TOKENS, team);
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.del(teamKey);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

}
