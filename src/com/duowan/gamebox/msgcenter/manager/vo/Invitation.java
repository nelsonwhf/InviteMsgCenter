/**
 * 
 */
package com.duowan.gamebox.msgcenter.manager.vo;

import java.util.Set;

/**
 * @author zhangtao.robin
 * 
 */
public class Invitation {

	private String inviteId;
	private Long inviteTimestamp;
	private String atTeam;

	private String fromUserId;
	private String fromUserType;
	private String fromDisplayName;

	private int fromYyUid;

	private Set<InvitationDetail> details;
	private String datas;

	/**
	 * @return the inviteId
	 */
	public String getInviteId() {
		return inviteId;
	}

	/**
	 * @param inviteId
	 *            the inviteId to set
	 */
	public void setInviteId(String inviteId) {
		this.inviteId = inviteId;
	}

	/**
	 * @return the inviteTimestamp
	 */
	public Long getInviteTimestamp() {
		return inviteTimestamp;
	}

	/**
	 * @param inviteTimestamp
	 *            the inviteTimestamp to set
	 */
	public void setInviteTimestamp(Long inviteTimestamp) {
		this.inviteTimestamp = inviteTimestamp;
	}

	/**
	 * @return the atTeam
	 */
	public String getAtTeam() {
		return atTeam;
	}

	/**
	 * @param atTeam
	 *            the atTeam to set
	 */
	public void setAtTeam(String atTeam) {
		this.atTeam = atTeam;
	}

	/**
	 * @return the fromUserId
	 */
	public String getFromUserId() {
		return fromUserId;
	}

	/**
	 * @param fromUserId
	 *            the fromUserId to set
	 */
	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	/**
	 * @return the fromUserType
	 */
	public String getFromUserType() {
		return fromUserType;
	}

	/**
	 * @param fromUserType
	 *            the fromUserType to set
	 */
	public void setFromUserType(String fromUserType) {
		this.fromUserType = fromUserType;
	}

	/**
	 * @return the fromDisplayName
	 */
	public String getFromDisplayName() {
		return fromDisplayName;
	}

	/**
	 * @param fromDisplayName
	 *            the fromDisplayName to set
	 */
	public void setFromDisplayName(String fromDisplayName) {
		this.fromDisplayName = fromDisplayName;
	}

	/**
	 * @return the details
	 */
	public Set<InvitationDetail> getDetails() {
		return details;
	}

	/**
	 * @param details
	 *            the details to set
	 */
	public void setDetails(Set<InvitationDetail> details) {
		this.details = details;
	}

	/**
	 * @return the datas
	 */
	public String getDatas() {
		return datas;
	}

	/**
	 * @param datas
	 *            the datas to set
	 */
	public void setDatas(String datas) {
		this.datas = datas;
	}

	/**
	 * @return the fromYyUid
	 */
	public int getFromYyUid() {
		return fromYyUid;
	}

	/**
	 * @param fromYyUid
	 *            the fromYyUid to set
	 */
	public void setFromYyUid(int fromYyUid) {
		this.fromYyUid = fromYyUid;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Invitation [inviteId=").append(inviteId).append(", inviteTimestamp=")
				.append(inviteTimestamp).append(", atTeam=").append(atTeam).append(", fromUserId=")
				.append(fromUserId).append(", fromUserType=").append(fromUserType)
				.append(", fromDisplayName=").append(fromDisplayName).append(", fromYyUid=")
				.append(fromYyUid).append(", details=").append(details).append(", datas=")
				.append(datas).append("]");
		return builder.toString();
	}

}
