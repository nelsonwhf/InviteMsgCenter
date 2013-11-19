/**
 * 
 */
package com.duowan.gamebox.msgcenter.msg.vo;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author zhangtao.robin
 * 
 */
public class LoginRequestMsg {
	@JsonProperty(value = "typ")
	private String gameType;

	@JsonProperty(value = "game_uid")
	private String gameUidEncrypt;

	@JsonProperty(value = "display_name")
	private String displayName;

	/**
	 * @return the gameType
	 */
	public String getGameType() {
		return gameType;
	}

	/**
	 * @param gameType
	 *            the gameType to set
	 */
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	/**
	 * @return the gameUid
	 */
	public String getGameUidEncrypt() {
		return gameUidEncrypt;
	}

	/**
	 * @param gameUid
	 *            the gameUid to set
	 */
	public void setGameUidEncrypt(String gameUid) {
		this.gameUidEncrypt = gameUid;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoginRequestMsg [gameType=").append(gameType).append(", gameUidEncrypt=")
				.append(gameUidEncrypt).append(", displayName=").append(displayName).append("]");
		return builder.toString();
	}

}
