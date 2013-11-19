/**
 * 
 */
package com.duowan.gamebox.msgcenter.msg.response;

import com.duowan.gamebox.msgcenter.msg.AbstractMessage;
import com.duowan.gamebox.msgcenter.msg.vo.QueryPartnersRsponseMsg;

/**
 * @author zhangtao.robin
 * 
 */
public class QueryPartnersResponse extends AbstractMessage<QueryPartnersRsponseMsg> {

	public QueryPartnersResponse() {
		super();
		setCode(QUERY_PARTNERS_CODE);
	}

}
