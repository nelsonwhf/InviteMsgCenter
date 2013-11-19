package com.duowan.gamebox.msgcenter.redis;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.Pool;

public class RedisPoolFactory {
	private Pool<Jedis> jedisPool;
	private RedisConfig redisConfig;

	private RedisPoolFactory(RedisConfig redisConfig) {
		super();
		this.redisConfig = redisConfig;
		this.jedisPool = new JedisPool(new JedisPoolConfig(), this.redisConfig.getRedisHost(),
				this.redisConfig.getRedisPort(), this.redisConfig.getRedisTimeout(),
				this.redisConfig.getRedisPassword(), this.redisConfig.getRedisDb());
	}

	/**
	 * @return the jedisPool
	 */
	public Pool<Jedis> getJedisPool() {
		return jedisPool;
	}

	/**
	 * @return the redisConfig
	 */
	public RedisConfig getRedisConfig() {
		return redisConfig;
	}

	private static final Map<RedisConfig, RedisPoolFactory> redisPoolFactoryMap = new HashMap<RedisConfig, RedisPoolFactory>();

	public static final RedisPoolFactory get(RedisConfig redisConfig) {
		RedisPoolFactory factory = redisPoolFactoryMap.get(redisConfig);
		if (factory == null) {
			factory = new RedisPoolFactory(redisConfig);
			redisPoolFactoryMap.put(redisConfig, factory);
		}
		return factory;
	}
}
