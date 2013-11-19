package com.duowan.gamebox.msgcenter.manager.impl;

import java.io.IOException;
import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

import com.duowan.gamebox.msgcenter.manager.UserManager;
import com.duowan.gamebox.msgcenter.manager.vo.User;
import com.duowan.gamebox.msgcenter.redis.NameSpace;
import com.duowan.gamebox.msgcenter.redis.RedisUtils;

public class UserManagerImpl implements UserManager {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private Pool<Jedis> jedisPool;
	private ObjectMapper jsonObjectMapper;

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
	 * @return the jsonObjectMapper
	 */
	public ObjectMapper getJsonObjectMapper() {
		return jsonObjectMapper;
	}

	/**
	 * @param jsonObjectMapper
	 *            the jsonObjectMapper to set
	 */
	public void setJsonObjectMapper(ObjectMapper jsonObjectMapper) {
		this.jsonObjectMapper = jsonObjectMapper;
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.UserManager#generateToken(com.duowan.gamebox.msgcenter.manager.vo.User)
	 */
	@Override
	public void generateToken(User user) {
		user.setToken(UUID.randomUUID().toString());
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.UserManager#getUserByToken(java.lang.String)
	 */
	@Override
	public User getUserByToken(String token) {
		String tokenKey = RedisUtils.createKey(NameSpace.TOKEN_MAPTO_USER, token);
		String jsonStr = null;
		Jedis jedis = jedisPool.getResource();
		try {
			jsonStr = jedis.get(tokenKey);
		} finally {
			jedisPool.returnResource(jedis);
		}
		if (jsonStr == null) {
			return null;
		}
		try {
			User user = jsonObjectMapper.readValue(jsonStr, User.class);
			return user;
		} catch (IOException e) {
			log.error("Read user from json failed: " + e, e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.UserManager#getUserByIdAndType(java.lang.String, java.lang.String)
	 */
	@Override
	public User getUserByIdAndType(String userId, String userType) {
		// find token first
		String token = getTokenByIdAndType(userId, userType);
		// then find by token
		return (token == null) ? null : getUserByToken(token);
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.UserManager#getTokenByIdAndType(java.lang.String, java.lang.String)
	 */
	@Override
	public String getTokenByIdAndType(String userId, String userType) {
		String userKey = RedisUtils.createKey(NameSpace.USER_MAPTO_TOKEN, userId, userType);
		Jedis jedis = jedisPool.getResource();
		String token = null;
		try {
			token = jedis.get(userKey);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return token;
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.UserManager#addUser(com.duowan.gamebox.msgcenter.manager.vo.User)
	 */
	@Override
	public void addUser(User user) {
		String userKey = RedisUtils.createKey(NameSpace.USER_MAPTO_TOKEN, user.getUserId(),
				user.getUserType());
		String tokenKey = RedisUtils.createKey(NameSpace.TOKEN_MAPTO_USER, user.getToken());
		if (log.isInfoEnabled()) {
			log.info("Add user: userKey=" + userKey + ", tokenKey=" + tokenKey);
		}

		String jsonStr = null;
		try {
			jsonStr = jsonObjectMapper.writeValueAsString(user);
		} catch (IOException e) {
			// should not happen
			log.error("Write user to json failed: " + e, e);
			return;
		}

		Jedis jedis = jedisPool.getResource();
		try {
			jedis.set(userKey, user.getToken());
			jedis.set(tokenKey, jsonStr);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.UserManager#removeUser(com.duowan.gamebox.msgcenter.manager.vo.User)
	 */
	@Override
	public void removeUser(User user) {
		String userKey = RedisUtils.createKey(NameSpace.USER_MAPTO_TOKEN, user.getUserId(),
				user.getUserType());
		String tokenKey = RedisUtils.createKey(NameSpace.TOKEN_MAPTO_USER, user.getToken());
		if (log.isInfoEnabled()) {
			log.info("Remove user: userKey=" + userKey + ", tokenKey=" + tokenKey);
		}
		Jedis jedis = jedisPool.getResource();
		try {
			String token = jedis.get(userKey);
			if (user.getToken().equals(token)) {
				jedis.del(userKey);
			}
			jedis.del(tokenKey);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.UserManager#removeUserByToken(java.lang.String)
	 */
	@Override
	public User removeUserByToken(String token) {
		User user = getUserByToken(token);
		if (user != null) {
			removeUser(user);
		}
		return user;
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.UserManager#removeUserByIdAndType(java.lang.String, java.lang.String)
	 */
	@Override
	public User removeUserByIdAndType(String userId, String userType) {
		User user = getUserByIdAndType(userId, userType);
		if (user != null) {
			removeUser(user);
		}
		return user;
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.UserManager#updateUser(com.duowan.gamebox.msgcenter.manager.vo.User)
	 */
	@Override
	public void updateUser(User user) {
		String tokenKey = RedisUtils.createKey(NameSpace.TOKEN_MAPTO_USER, user.getToken());
		if (log.isInfoEnabled()) {
			log.info("Update user: tokenKey=" + tokenKey);
		}

		String jsonStr = null;
		try {
			jsonStr = jsonObjectMapper.writeValueAsString(user);
		} catch (IOException e) {
			// should not happen
			log.error("Write user to json failed: " + e, e);
			return;
		}

		Jedis jedis = jedisPool.getResource();
		try {
			jedis.set(tokenKey, jsonStr);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}
}
