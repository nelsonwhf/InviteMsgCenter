/**
 * 
 */
package com.duowan.gamebox.msgcenter.manager.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

import com.duowan.gamebox.msgcenter.manager.InvitationManager;
import com.duowan.gamebox.msgcenter.manager.vo.Invitation;
import com.duowan.gamebox.msgcenter.manager.vo.InvitationDetail;
import com.duowan.gamebox.msgcenter.manager.vo.User;
import com.duowan.gamebox.msgcenter.msg.vo.NotifyInvitationMsg;
import com.duowan.gamebox.msgcenter.msg.vo.NotifyReplyMsg;
import com.duowan.gamebox.msgcenter.queue.NotifyInvitationMsgInternal;
import com.duowan.gamebox.msgcenter.queue.NotifyReplyMsgInternal;
import com.duowan.gamebox.msgcenter.redis.NameSpace;
import com.duowan.gamebox.msgcenter.redis.RedisUtils;

/**
 * @author zhangtao.robin
 * 
 */
public class InvitationManagerImpl implements InvitationManager {
	private static final String SQL_INSERT_INVITATION = "INSERT INTO msg_invitation (inviteId,inviteTimestamp,atTeam,fromUserId,fromUserType,fromDisplayName,fromYyUid,datas)"
			+
			" VALUES (?,?,?,?,?,?,?,?)";
	private static final String SQL_INSERT_INVITATION_DETAIL = "INSERT INTO msg_invitation_detail (inviteId,toUserId,toUserType,toDisplayName,replyTimestamp,replyTag,"
			+
			"notifyReplyTimestamp,notifyReplyTag,replyExtra,toTimestamp,toTag) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

	private static final String SQL_SELECT_INVITATION = "SELECT * FROM msg_invitation WHERE inviteId=?";
	private static final String SQL_SELECT_INVITATION_DETAIL = "SELECT * FROM msg_invitation_detail WHERE inviteId=?";

	private static final String SQL_UPDATE_INVITATION_DETAIL = "UPDATE msg_invitation_detail SET toUserId=?,toUserType=?,toDisplayName=?,replyTimestamp=?,replyTag=?,"
			+
			"notifyReplyTimestamp=?,notifyReplyTag=?,replyExtra=?,toTimestamp=?,toTag=? WHERE inviteId=?";

	private static final String SQL_QUERY_UNREAD_INVITATIONS = "SELECT b.* FROM "
			+
			" (SELECT DISTINCT inviteId FROM msg_invitation_detail WHERE toUserId=? AND toUserType=? AND toTag=-1) a"
			+
			" JOIN msg_invitation b ON a.inviteId=b.inviteId WHERE b.inviteTimestamp>=? ORDER BY b.inviteTimestamp DESC;";

	private static final String SQL_QUERY_UNREAD_REPLIES =
			"SELECT a.inviteTimestamp, a.atTeam, b.* FROM"
					+
					" (SELECT inviteId, inviteTimestamp, atTeam FROM msg_invitation WHERE fromUserId=? AND fromUserType=? AND inviteTimestamp>=?) a "
					+
					"  JOIN msg_invitation_detail b ON a.inviteId=b.inviteId WHERE b.notifyReplyTag=-1 AND b.replyTag<>-1;";
	/*
		private static final String SQL_COUNT_SEND_INVITATIONS = "SELECT b.notifyReplyTag, count(b.inviteId) as num FROM"
				+
				" (SELECT inviteId FROM msg_invitation WHERE fromUserId=? AND fromUserType=? AND inviteTimestamp>=?) a"
				+
				" JOIN msg_invitation_detail b ON a.inviteId=b.inviteId GROUP BY b.notifyReplyTag;";

		private static final String SQL_QUERY_SEND_INVITATIONS =
				"SELECT a.*, b.toUserId, b.toUserType, b.toDisplayName, b.replyTimestamp, b.replyTag, b.notifyReplyTimestamp, b.notifyReplyTag, b.replyExtra FROM"
						+
						" (SELECT * FROM msg_invitation WHERE fromUserId=? AND fromUserType=? AND inviteTimestamp>=?) a"
						+
						" JOIN msg_invitation_detail b ON a.inviteId=b.inviteId ORDER BY a.inviteTimestamp DESC LIMIT ? OFFSET ?;";

		private static final String SQL_COUNT_RECV_INVITATIONS = "SELECT a.replyTag, count(a.inviteId) as num FROM"
				+
				" (SELECT inviteId, replyTag FROM msg_invitation_detail WHERE toUserId=? AND toUserType=? AND (replyTag=-1 OR replyTimestamp>=?)) a"
				+
				" JOIN msg_invitation b ON a.inviteId=b.inviteId WHERE b.inviteTimestamp>=? GROUP BY a.replyTag;";

		private static final String SQL_QUERY_RECV_INVITATIONS =
				"SELECT b.*, a.toUserId, a.toUserType, a.toDisplayName, a.replyTimestamp, a.replyTag, a.notifyReplyTimestamp, a.notifyReplyTag, a.replyExtra FROM"
						+
						" (SELECT * FROM msg_invitation_detail WHERE toUserId=? AND toUserType=? AND (replyTag=-1 OR replyTimestamp>=?)) a"
						+
						" JOIN msg_invitation b ON a.inviteId=b.inviteId WHERE b.inviteTimestamp>=? ORDER BY b.inviteTimestamp DESC LIMIT ? OFFSET ?;";
	*/
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private DataSource dataSource;
	private Pool<Jedis> jedisPool;
	private ObjectMapper jsonObjectMapper;

	private int cacheTimeOut = 600;

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.InvitationManager#addInvitation(com.duowan.gamebox.msgcenter.manager.vo.Invitation)
	 */
	@Override
	public void addInvitation(Invitation invitation) {
		// add to cache
		setInvitationToCache(invitation);
		// add to DB
		addInvitationToDB(invitation);
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.InvitationManager#getInvitationById(java.lang.String)
	 */
	@Override
	public Invitation getInvitationById(String inviteId) {
		// try from cache
		Invitation invitation = getInvitationFromCache(inviteId);
		if (invitation == null) {
			// try from DB
			invitation = getInvitationFromDB(inviteId);
			if (invitation != null) {
				// add it to cache
				setInvitationToCache(invitation);
			}
		}
		return invitation;
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.impl.InvitationManager#updateInvitationDetail(com.duowan.gamebox.msgcenter.manager.vo.InvitationDetail)
	 */
	@Override
	public Invitation updateInvitationDetail(InvitationDetail detail) {
		Invitation invitation = getInvitationById(detail.getInviteId());
		if (invitation != null) {
			// update it in cache
			Set<InvitationDetail> details = invitation.getDetails();
			if (details == null) {
				details = new HashSet<InvitationDetail>();
				invitation.setDetails(details);
			}
			// update element of set
			details.remove(detail);
			details.add(detail);
			setInvitationToCache(invitation);

			// update it in DB
			updateInvitationDetailToDB(detail);
		}
		return invitation;
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.InvitationManager#removeInvitationFromCache(com.duowan.gamebox.msgcenter.manager.vo.Invitation)
	 */
	public void removeInvitationFromCache(Invitation invitation) {
		String inviteKey = RedisUtils.createKey(NameSpace.INVITATION_KEY, invitation.getInviteId());
		if (log.isInfoEnabled()) {
			log.info("removeInvitationFromCache invitKey: " + inviteKey);
		}

		Jedis jedis = jedisPool.getResource();
		try {
			jedis.del(inviteKey);
			if (log.isDebugEnabled()) {
				log.debug("removeInvitationFromCache success: " + invitation);
			}
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.InvitationManager#queryUnreadInvitations(com.duowan.gamebox.msgcenter.manager.vo.User, int)
	 */
	@Override
	public List<NotifyInvitationMsgInternal> queryUnreadInvitations(User toUser, int secondsIn) {
		if (dataSource == null)
			return null;

		List<NotifyInvitationMsgInternal> listMsgs = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rst = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(SQL_QUERY_UNREAD_INVITATIONS);
			int i = 1;
			stmt.setString(i++, toUser.getUserId());
			stmt.setString(i++, toUser.getUserType());
			stmt.setLong(i++, System.currentTimeMillis() - (secondsIn * 1000));
			rst = stmt.executeQuery();

			while (rst.next()) {
				if (listMsgs == null) {
					listMsgs = new ArrayList<NotifyInvitationMsgInternal>();
				}

				NotifyInvitationMsgInternal msg = new NotifyInvitationMsgInternal();
				NotifyInvitationMsg noti = msg.getNotifyInvitationMsg();
				// inviteId,inviteTimestamp,atTeam,fromUserId,fromUserType,fromDisplayName,fromYyUid,datas
				noti.setInviteId(rst.getString("inviteId"));
				noti.setInviteTimestamp(rst.getLong("inviteTimestamp"));
				noti.setSummary(rst.getString("atTeam"));
				msg.setFromUserId(rst.getString("fromUserId"));
				msg.setFromUserType(rst.getString("fromUserType"));
				noti.setFromDisplayName(rst.getString("fromDisplayName"));
				noti.setDatas(rst.getString("datas"));

				listMsgs.add(msg);
			}

			if (log.isDebugEnabled()) {
				log.debug("queryUnreadInvitations success: " + listMsgs);
			}
			return listMsgs;
		} catch (SQLException e) {
			log.error("queryUnreadInvitations failed: " + e, e);
			return null;
		} finally {
			closeResultSet(rst);
			closeStatement(stmt);
			closeConn(conn);
		}
	}

	/* (non-Javadoc)
	 * @see com.duowan.gamebox.msgcenter.manager.InvitationManager#queryUnreadReplies(com.duowan.gamebox.msgcenter.manager.vo.User, int)
	 */
	@Override
	public List<NotifyReplyMsgInternal> queryUnreadReplies(User toUser, int secondsIn) {
		if (dataSource == null)
			return null;

		List<NotifyReplyMsgInternal> listMsgs = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rst = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(SQL_QUERY_UNREAD_REPLIES);
			int i = 1;
			stmt.setString(i++, toUser.getUserId());
			stmt.setString(i++, toUser.getUserType());
			stmt.setLong(i++, System.currentTimeMillis() - (secondsIn * 1000));
			rst = stmt.executeQuery();

			while (rst.next()) {
				if (listMsgs == null) {
					listMsgs = new ArrayList<NotifyReplyMsgInternal>();
				}

				NotifyReplyMsgInternal msg = new NotifyReplyMsgInternal();
				NotifyReplyMsg noti = msg.getNotifyReplyMsg();
				// inviteTimestamp,atTeam,inviteId,toUserId,toUserType,toDisplayName,replyTimestamp,replyTag,notifyReplyTimestamp,notifyReplyTag
				msg.setReplyFromUserId(rst.getString("toUserId"));
				msg.setReplyFromUserType(rst.getString("toUserType"));
				noti.setInviteId(rst.getString("inviteId"));
				noti.setInviteTimestamp(rst.getLong("inviteTimestamp"));
				noti.setReplyFromDisplayName(rst.getString("toDisplayName"));
				noti.setReplyTimestamp(rst.getLong("replyTimestamp"));
				noti.setSummary(rst.getString("atTeam"));
				noti.setTag(rst.getInt("replyTag"));
				noti.setExtra(rst.getString("replyExtra"));

				listMsgs.add(msg);
			}

			if (log.isDebugEnabled()) {
				log.debug("queryUnreadInvitations success: " + listMsgs);
			}
			return listMsgs;
		} catch (SQLException e) {
			log.error("queryUnreadInvitations failed: " + e, e);
			return null;
		} finally {
			closeResultSet(rst);
			closeStatement(stmt);
			closeConn(conn);
		}
	}

	private void setInvitationToCache(Invitation invitation) {
		String inviteKey = RedisUtils.createKey(NameSpace.INVITATION_KEY, invitation.getInviteId());
		if (log.isInfoEnabled()) {
			log.info("setInvitationToCache invitKey: " + inviteKey);
		}
		String jsonStr = genInvitationJson(invitation);
		if (jsonStr != null) {
			Jedis jedis = jedisPool.getResource();
			try {
				jedis.set(inviteKey, jsonStr);
				jedis.expire(inviteKey, cacheTimeOut);
				if (log.isDebugEnabled()) {
					log.debug("setInvitationToCache success: " + invitation);
				}
			} finally {
				jedisPool.returnResource(jedis);
			}
		}
	}

	private void addInvitationToDB(Invitation invitation) {
		if (dataSource == null)
			return;
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		String inviteId = invitation.getInviteId();
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(SQL_INSERT_INVITATION);
			int i = 1;
			stmt.setString(i++, inviteId);
			stmt.setLong(i++, invitation.getInviteTimestamp());
			stmt.setString(i++, invitation.getAtTeam());
			stmt.setString(i++, invitation.getFromUserId());
			stmt.setString(i++, invitation.getFromUserType());
			stmt.setString(i++, invitation.getFromDisplayName());
			stmt.setInt(i++, invitation.getFromYyUid());
			stmt.setString(i++, invitation.getDatas());
			stmt.execute();

			Set<InvitationDetail> details = invitation.getDetails();
			if (details != null && details.size() != 0) {
				stmt2 = conn.prepareStatement(SQL_INSERT_INVITATION_DETAIL);
				for (InvitationDetail detail : details) {
					i = 1;
					stmt2.setString(i++, inviteId);
					stmt2.setString(i++, detail.getToUserId());
					stmt2.setString(i++, detail.getToUserType());
					stmt2.setString(i++, detail.getToDisplayName());
					stmt2.setObject(i++, detail.getReplyTimestamp());
					stmt2.setInt(i++, detail.getReplyTag());
					stmt2.setObject(i++, detail.getNotifyReplyTimestamp());
					stmt2.setInt(i++, detail.getNotifyReplyTag());
					stmt2.setString(i++, detail.getReplyExtra());
					stmt2.setObject(i++, detail.getToTimestamp());
					stmt2.setInt(i++, detail.getToTag());
					stmt2.execute();
				}
			}
			if (log.isDebugEnabled()) {
				log.debug("addInvitationToDB success: " + invitation);
			}
		} catch (SQLException e) {
			log.error("addInvitationToDB failed: " + e, e);
			return;
		} finally {
			closeStatement(stmt2);
			closeStatement(stmt);
			closeConn(conn);
		}
	}

	private Invitation getInvitationFromCache(String inviteId) {
		String invitKey = RedisUtils.createKey(NameSpace.INVITATION_KEY, inviteId);
		if (log.isInfoEnabled()) {
			log.info("getInvitationFromCache invitKey: " + invitKey);
		}

		String jsonStr = null;
		Jedis jedis = jedisPool.getResource();
		try {
			jsonStr = jedis.get(invitKey);
		} finally {
			jedisPool.returnResource(jedis);
		}

		Invitation invitation = null;
		if (jsonStr != null) {
			try {
				invitation = jsonObjectMapper.readValue(jsonStr, Invitation.class);
			} catch (IOException e) {
				log.error("getInvitationFromCache failed: " + e, e);
			}
		}
		return invitation;
	}

	private Invitation getInvitationFromDB(String inviteId) {
		if (dataSource == null)
			return null;

		Invitation invitation = null;

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rst = null;
		PreparedStatement stmt2 = null;
		ResultSet rst2 = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(SQL_SELECT_INVITATION);
			stmt.setString(1, inviteId);
			rst = stmt.executeQuery();
			invitation = convInvitationFromResultSet(rst);

			if (invitation != null) {
				stmt2 = conn.prepareStatement(SQL_SELECT_INVITATION_DETAIL);
				stmt2.setString(1, inviteId);
				rst2 = stmt2.executeQuery();
				Set<InvitationDetail> details = convInvitationDetailFromResultSet(rst2);
				invitation.setDetails(details);
			}

			if (log.isDebugEnabled()) {
				log.debug("getInvitationFromDB success: " + invitation);
			}
			return invitation;
		} catch (SQLException e) {
			log.error("getInvitationFromDB failed: " + e, e);
			return null;
		} finally {
			closeResultSet(rst);
			closeResultSet(rst2);
			closeStatement(stmt2);
			closeStatement(stmt);
			closeConn(conn);
		}
	}

	private void updateInvitationDetailToDB(InvitationDetail detail) {
		if (dataSource == null)
			return;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(SQL_UPDATE_INVITATION_DETAIL);
			int i = 1;
			stmt.setString(i++, detail.getToUserId());
			stmt.setString(i++, detail.getToUserType());
			stmt.setString(i++, detail.getToDisplayName());
			stmt.setObject(i++, detail.getReplyTimestamp());
			stmt.setInt(i++, detail.getReplyTag());
			stmt.setObject(i++, detail.getNotifyReplyTimestamp());
			stmt.setInt(i++, detail.getNotifyReplyTag());
			stmt.setString(i++, detail.getReplyExtra());
			stmt.setObject(i++, detail.getToTimestamp());
			stmt.setInt(i++, detail.getToTag());
			stmt.setString(i++, detail.getInviteId());
			stmt.execute();

			if (log.isDebugEnabled()) {
				log.debug("updateInvitationDetail success: " + detail);
			}
		} catch (SQLException e) {
			log.error("updateInvitationDetail failed: " + e, e);
			return;
		} finally {
			closeStatement(stmt);
			closeConn(conn);
		}
	}

	private Invitation convInvitationFromResultSet(ResultSet rst) throws SQLException {
		if (rst.next()) {
			Invitation invitation = new Invitation();
			// inviteId,inviteTimestamp,atTeam,fromUserId,fromUserType,fromDisplayName,fromYyUid,datas
			invitation.setInviteId(rst.getString("inviteId"));
			invitation.setInviteTimestamp(rst.getLong("inviteTimestamp"));
			invitation.setAtTeam(rst.getString("atTeam"));
			invitation.setFromUserId(rst.getString("fromUserId"));
			invitation.setFromUserType(rst.getString("fromUserType"));
			invitation.setFromDisplayName(rst.getString("fromDisplayName"));
			invitation.setFromYyUid(rst.getInt("fromYyUid"));
			invitation.setDatas(rst.getString("datas"));
			return invitation;
		}
		return null;
	}

	private Set<InvitationDetail> convInvitationDetailFromResultSet(ResultSet rst)
			throws SQLException {
		Set<InvitationDetail> details = null;
		while (rst.next()) {
			if (details == null) {
				details = new HashSet<InvitationDetail>();
			}
			InvitationDetail detail = new InvitationDetail();
			// inviteId,toUserId,toUserType,toDisplayName,replyTimestamp,replyTag,notifyReplyTimestamp,notifyReplyTag,toTimestamp,toTag
			detail.setInviteId(rst.getString("inviteId"));
			detail.setToUserId(rst.getString("toUserId"));
			detail.setToUserType(rst.getString("toUserType"));
			detail.setToDisplayName(rst.getString("toDisplayName"));
			long tmp = rst.getLong("replyTimestamp");
			if (tmp != 0) {
				detail.setReplyTimestamp(tmp);
			}
			detail.setReplyTag(rst.getInt("replyTag"));
			tmp = rst.getLong("notifyReplyTimestamp");
			if (tmp != 0) {
				detail.setNotifyReplyTimestamp(tmp);
			}
			detail.setNotifyReplyTag(rst.getInt("notifyReplyTag"));
			tmp = rst.getLong("toTimestamp");
			if (tmp != 0) {
				detail.setToTimestamp(tmp);
			}
			detail.setToTag(rst.getInt("toTag"));

			details.add(detail);
		}
		return details;
	}

	private void closeResultSet(ResultSet rst) {
		if (rst != null) {
			try {
				rst.close();
			} catch (SQLException e) {
			}
		}
	}

	private void closeStatement(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}

	private void closeConn(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
	}

	private String genInvitationJson(Invitation invitation) {
		try {
			return jsonObjectMapper.writeValueAsString(invitation);
		} catch (IOException e) {
			// should not happen
			log.error("Write invitation to json failed: " + e, e);
			return null;
		}
	}

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

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
	 * @return the cacheTimeOut
	 */
	public int getCacheTimeOut() {
		return cacheTimeOut;
	}

	/**
	 * @param cacheTimeOut
	 *            the cacheTimeOut to set
	 */
	public void setCacheTimeOut(int cacheTimeOut) {
		this.cacheTimeOut = cacheTimeOut;
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

}
