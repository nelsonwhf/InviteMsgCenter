package com.duowan.gamebox.msgcenter.rate.impl;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

import com.duowan.gamebox.msgcenter.manager.exception.ExceedLimitException;
import com.duowan.gamebox.msgcenter.manager.vo.RateLimitation;
import com.duowan.gamebox.msgcenter.rate.RateLimitManager;

public class RateLimitManagerImpl implements RateLimitManager {

	private Pool<Jedis> jedisPool;

	private RateLimitation rateLimitation;

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
	 * @return the rateLimitation
	 */
	public RateLimitation getRateLimitation() {
		return rateLimitation;
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.RateLimitManager#setRateLimitation(com.duowan.gamebox.msgcenter.manager.vo.RateLimitation)
	 */
	@Override
	public void setRateLimitation(RateLimitation rateLimitation) {
		this.rateLimitation = rateLimitation;
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.RateLimitManager#limitCall(java.lang.String)
	 */
	@Override
	public void limitCall(String controlKey) throws ExceedLimitException {
		if (rateLimitation == null) {
			// no limitation
			return;
		}

		Jedis jedis = jedisPool.getResource();
		try {
			String countStr = jedis.get(controlKey);
			int current = (countStr == null) ? 0 : Integer.parseInt(countStr);
			if (current >= rateLimitation.getRate()) {
				throw new ExceedLimitException("Exceed limits, current: " + current);
			}
			long next = jedis.incr(controlKey);
			if (next == 1L) {
				jedis.expire(controlKey, rateLimitation.getDurationSeconds());
			}
		} finally {
			jedisPool.returnResource(jedis);
		}

	}

}
