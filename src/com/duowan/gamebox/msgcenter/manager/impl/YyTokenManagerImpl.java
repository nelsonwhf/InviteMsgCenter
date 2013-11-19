/**
 * 
 */
package com.duowan.gamebox.msgcenter.manager.impl;

import com.duowan.common.loadbalance.FailOverProcessor;
import com.duowan.common.loadbalance.ProcessException;
import com.duowan.gamebox.msgcenter.manager.YyTokenManager;
import com.duowan.gamebox.msgcenter.manager.exception.InvalidYyTokenException;
import com.duowan.udb.service.ResCode;
import com.duowan.udb.service.TokenEncodingType;
import com.duowan.udb.service.VerifyAppTokenReq;
import com.duowan.udb.service.VerifyAppTokenRes;

/**
 * @author zhangtao.robin
 * 
 */
public class YyTokenManagerImpl implements YyTokenManager {

	private static final String APPID_5060 = "5060";

	private boolean debugMode = false;

	private FailOverProcessor<VerifyAppTokenReq, VerifyAppTokenRes> failOverProcessor;

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.YyTokenManager#verifyYyToken(java.lang.String)
	 */
	@Override
	public int verifyYyToken(String yyToken) throws InvalidYyTokenException {
		if (debugMode) {
			return 0;
		}

		VerifyAppTokenReq req = new VerifyAppTokenReq();
		req.setAppid(APPID_5060);
		req.setYyuid(0);
		req.setEncoding_type(TokenEncodingType.BASE64_WITH_URL);
		req.setToken(yyToken);

		try {
			VerifyAppTokenRes resp = failOverProcessor.process(req);
			ResCode respCode = ResCode.findByValue(resp.getRescode());
			if (respCode == ResCode.SUI_VERIFY_SUCCESS) {
				return resp.getYyuid();
			}
			throw new InvalidYyTokenException("Verify token failed: " + resp.getRescode());
		} catch (ProcessException e) {
			Throwable cause = e.getCause();
			if (cause != null) {
				throw new InvalidYyTokenException("Verify token failed: " + cause, cause);
			} else {
				throw new InvalidYyTokenException("Verify token failed: " + e, e);
			}
		}
		/*
				TTransport tTrans = new TFramedTransport(new TSocket(host, port, timeout));

				try {
					tTrans.open();
					TProtocol tProtocol = new TBinaryProtocol(tTrans);
					Client client = new Client(tProtocol);
					VerifyAppTokenRes resp = client.lg_secuserinfo_verify_apptoken(req);
					ResCode respCode = ResCode.findByValue(resp.getRescode());
					if (respCode == ResCode.SUI_VERIFY_SUCCESS) {
						return resp.getYyuid();
					}
					throw new InvalidYyTokenException("Verify token failed: " + respCode);
				} catch (TException e) {
					throw new InvalidYyTokenException("Verify token failed: " + e, e);
				} finally {
					tTrans.close();
				}
		*/
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

	/**
	 * @return the failOverProcessor
	 */
	public FailOverProcessor<VerifyAppTokenReq, VerifyAppTokenRes> getFailOverProcessor() {
		return failOverProcessor;
	}

	/**
	 * @param failOverProcessor
	 *            the failOverProcessor to set
	 */
	public void setFailOverProcessor(
			FailOverProcessor<VerifyAppTokenReq, VerifyAppTokenRes> failOverProcessor) {
		this.failOverProcessor = failOverProcessor;
	}

}
