package com.duowan.gamebox.msgcenter.manager;

import java.security.GeneralSecurityException;

import com.duowan.gamebox.msgcenter.msg.vo.LoginRequestMsg;

public interface UserCryptManager {

	public abstract String decryptUserId(LoginRequestMsg loginRequestMsg)
			throws GeneralSecurityException;

	public abstract String decryptUserId(String type, String uidEncrypt)
			throws GeneralSecurityException;

	public abstract String encryptUserId(String type, String userId)
			throws GeneralSecurityException;

	public abstract void initCryptKeys();

}