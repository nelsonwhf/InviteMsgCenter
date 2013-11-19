/**
 * 
 */
package com.duowan.gamebox.msgcenter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

import com.duowan.gamebox.msgcenter.listener.SingletonVars;
import com.duowan.gamebox.msgcenter.msg.response.ErrorResponse;
import com.duowan.gamebox.msgcenter.msg.response.ServerListResponse;
import com.duowan.gamebox.msgcenter.msg.vo.ServerListMsg;
import com.duowan.gamebox.msgcenter.redis.NameSpace;

/**
 * @author zhangtao.robin
 * 
 */
@Path("/servers")
public class ServerListService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getServerList() {
		Set<String> servers = null;
		Pool<Jedis> jedisPool = SingletonVars.get().getJedisPool();
		if (jedisPool != null) {
			Jedis jedis = jedisPool.getResource();
			try {
				servers = jedis.smembers(NameSpace.SERVERS_KEY);
			} finally {
				jedisPool.returnResource(jedis);
			}
		}
		if (servers == null || servers.isEmpty()) {
			ErrorResponse errorResponse = ErrorResponse
					.newInternalErrorResponse("Server List is Empty");
			return genRestfulErrorResponse(errorResponse);
		}

		String[] arr = servers.toArray(new String[servers.size()]);
		Random random = SingletonVars.get().getRandom();
		int startIndex = random.nextInt(arr.length);
		List<String> loopUrls = new ArrayList<String>(arr.length);
		List<String> requestUrls = new ArrayList<String>(arr.length);
		for (int i = startIndex; i < arr.length; i++) {
			loopUrls.add(arr[i] + "/loop");
			requestUrls.add(arr[i] + "/request");
		}
		for (int i = 0; i < startIndex; i++) {
			loopUrls.add(arr[i] + "/loop");
			requestUrls.add(arr[i] + "/request");
		}
		ServerListMsg msg = new ServerListMsg();
		msg.setLoopUrls(loopUrls);
		msg.setRequestUrls(requestUrls);
		ServerListResponse svrListRsp = new ServerListResponse();
		svrListRsp.setMsg(msg);

		return Response.status(200).entity(svrListRsp).build();
	}

	private Response genRestfulErrorResponse(ErrorResponse errorResponse) {
		if (log.isInfoEnabled()) {
			log.info("Send error response: " + errorResponse);
		}
		return Response.status(errorResponse.getCode()).entity(errorResponse).build();
	}

}
