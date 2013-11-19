/**
 * 
 */
package com.duowan.gamebox.msgcenter.manager.impl;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

import com.duowan.gamebox.msgcenter.manager.UserCryptManager;
import com.duowan.gamebox.msgcenter.manager.crypt.CryptTool;
import com.duowan.gamebox.msgcenter.msg.vo.LoginRequestMsg;
import com.duowan.gamebox.msgcenter.redis.NameSpace;
import com.duowan.gamebox.msgcenter.redis.RedisUtils;
import com.duowan.gamebox.msgcenter.util.HexUtils;

/**
 * @author zhangtao.robin
 * 
 */
public class UserCryptManagerImpl implements UserCryptManager {

	private CryptTool cryptTool = new CryptTool();
	private Pool<Jedis> jedisPool;

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

	private byte[] getUTF8Bytes(String s) {
		try {
			return s.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	private String makeUTF8String(byte[] bytes) {
		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	private byte[] getCryptKey(String userType) {
		String key = RedisUtils.createKey(NameSpace.CRYPY_KEY, userType);
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.get(getUTF8Bytes(key));
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.UserCryptManager#decryptUserId(com.duowan.gamebox.msgcenter.msg.vo.LoginRequestMsg)
	 */
	@Override
	public String decryptUserId(LoginRequestMsg loginRequestMsg) throws GeneralSecurityException {
		return this.decryptUserId(loginRequestMsg.getGameType(),
				loginRequestMsg.getGameUidEncrypt());
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.UserCryptManager#decryptUserId(java.lang.String, java.lang.String)
	 */
	@Override
	public String decryptUserId(String type, String uidEncrypt) throws GeneralSecurityException {
		byte[] cryptKey = getCryptKey(type);
		byte[] data = cryptTool.decrypt(cryptKey, HexUtils.convertBytes(uidEncrypt));
		return makeUTF8String(data);
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.UserCryptManager#encryptUserId(java.lang.String, java.lang.String)
	 */
	@Override
	public String encryptUserId(String type, String userId) throws GeneralSecurityException {
		byte[] cryptKey = getCryptKey(type);
		byte[] uidEncrypt = cryptTool.encrypt(cryptKey, getUTF8Bytes(userId));
		return HexUtils.convertHexString(uidEncrypt);

	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.UserCryptManager#initCryptKeys()
	 */
	@Override
	public void initCryptKeys() {

		String[] types = new String[] { "lol" };
		byte[][] keys = new byte[][] { HexUtils
				.convertBytes("c9228b7c617693b5eecd67750eb14aa3") };

		Jedis jedis = jedisPool.getResource();
		try {
			for (int i = 0; i < types.length; i++) {
				String key = RedisUtils.createKey(NameSpace.CRYPY_KEY, types[i]);
				jedis.set(getUTF8Bytes(key), keys[i]);
			}
		} finally {
			jedisPool.returnResource(jedis);
		}
	}
}
