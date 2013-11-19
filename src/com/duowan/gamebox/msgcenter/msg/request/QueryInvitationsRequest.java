/**
 * 
 */
package com.duowan.gamebox.msgcenter.msg.request;

import com.duowan.gamebox.msgcenter.msg.AbstractMessage;
import com.duowan.gamebox.msgcenter.msg.vo.QueryInvitationsRequestMsg;

/**
 * @author zhangtao.robin
 * 
 */
public class QueryInvitationsRequest extends AbstractMessage<QueryInvitationsRequestMsg> {

	public QueryInvitationsRequest() {
		super();
		setCode(QUERY_INVITATIONS_CODE);
	}

}
