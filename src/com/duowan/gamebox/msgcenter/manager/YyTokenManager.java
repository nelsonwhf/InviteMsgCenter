package com.duowan.gamebox.msgcenter.manager;

import com.duowan.gamebox.msgcenter.manager.exception.InvalidYyTokenException;

public interface YyTokenManager {

	public abstract int verifyYyToken(String yyToken) throws InvalidYyTokenException;
}