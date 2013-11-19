package com.duowan.gamebox.msgcenter.rate;

import com.duowan.gamebox.msgcenter.manager.exception.ExceedLimitException;
import com.duowan.gamebox.msgcenter.manager.vo.RateLimitation;

public interface RateLimitManager {

	/**
	 * @param rateLimitation
	 *            the rateLimitation to set
	 */
	public abstract void setRateLimitation(RateLimitation rateLimitation);

	public abstract void limitCall(String controlKey) throws ExceedLimitException;

}