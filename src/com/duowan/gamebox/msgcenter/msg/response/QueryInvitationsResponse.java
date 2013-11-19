/**
 * 
 */
package com.duowan.gamebox.msgcenter.msg.response;

import com.duowan.gamebox.msgcenter.msg.AbstractMessage;
import com.duowan.gamebox.msgcenter.msg.vo.QueryInvitationsResponseMsg;

/**
 * @author zhangtao.robin
 * 
 */
public class QueryInvitationsResponse extends AbstractMessage<QueryInvitationsResponseMsg> {

	public QueryInvitationsResponse() {
		super();
		setCode(QUERY_INVITATIONS_CODE);
	}

}
