/**
 * 
 */
package com.duowan.gamebox.msgcenter.msg.request;

import com.duowan.gamebox.msgcenter.msg.AbstractMessage;
import com.duowan.gamebox.msgcenter.msg.vo.QueryPartnersRequestMsg;

/**
 * @author zhangtao.robin
 * 
 */
public class QueryPartnersRequest extends AbstractMessage<QueryPartnersRequestMsg> {

	public QueryPartnersRequest() {
		super();
		setCode(QUERY_PARTNERS_CODE);
	}

}
