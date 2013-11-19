package com.duowan.gamebox.msgcenter.serde;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duowan.gamebox.msgcenter.msg.AbstractMessage;
import com.duowan.gamebox.msgcenter.msg.request.JoinGameRequest;
import com.duowan.gamebox.msgcenter.msg.request.LoginRequest;
import com.duowan.gamebox.msgcenter.msg.request.SendInvitationRequest;

public class MessageDeserializer {

	private static final String MSG = "msg";

	private static final String CODE = "code";

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	private Map<Integer, MessageClassInfo> codeMapping = new HashMap<Integer, MessageClassInfo>();

	private ObjectMapper mapper = JsonObjectMapper.getObjectMapper();

	public AbstractMessage<?> deserialize(InputStream in) throws IOException {
		JsonNode jsonNode = mapper.readTree(in);
		return deserialize(jsonNode);
	}

	public AbstractMessage<?> deserialize(JsonNode jsonNode) throws IOException {
		JsonNode codeNode = jsonNode.get(CODE);
		if (codeNode != null) {
			int code = codeNode.getIntValue();
			MessageClassInfo info = codeMapping.get(code);
			if (info == null) {
				log.error("Cannot find mapping by code=" + code);
				throw new IOException("Cannot find mapping by code=" + code);
			}
			AbstractMessage<?> request = null;
			try {
				request = info.getMainClass().newInstance();
			} catch (Exception e) {
				log.error("MainClass().newInstance() error " + e, e);
				throw new IOException("newInstance error " + e);
			}

			JsonNode msgNode = jsonNode.get(MSG);
			if (msgNode != null) {
				Object msg = null;
				try {
					msg = mapper.readValue(msgNode, info.getMsgClass());
				} catch (Exception e) {
					log.error("readValue from msgNode error " + e, e);
					throw new IOException("readValue from msgNode error " + e);
				}
				try {
					info.getSetMsgMethod().invoke(request, msg);
				} catch (Exception e) {
					log.error("Invoke setMsg(object) error " + e, e);
					throw new IOException("Invoke setMsg(object) error " + e);
				}
			}
			return request;
		}

		log.error("Cannot find code in json " + jsonNode);
		throw new IOException("Cannot find code in json " + jsonNode);
	}

	public void addCodeMapping(int code, Class<? extends AbstractMessage<?>> clazz) {
		MessageClassInfo info = new MessageClassInfo(clazz);
		// System.out.println(info);
		if (log.isInfoEnabled()) {
			log.info("Add code=" + code + ", mapping=" + info);
		}
		codeMapping.put(code, info);
	}

	@SuppressWarnings("unchecked")
	public void addCodeMapping(AbstractMessage<?> message) {
		addCodeMapping(message.getCode(), (Class<? extends AbstractMessage<?>>) message.getClass());
	}

	public static void main(String[] args) {
		MessageDeserializer rd = new MessageDeserializer();
		rd.addCodeMapping(AbstractMessage.LOGIN_CODE, LoginRequest.class);
		rd.addCodeMapping(AbstractMessage.JOIN_GAME_CODE, JoinGameRequest.class);
		rd.addCodeMapping(new SendInvitationRequest());

		String msgStr = "{ \"code\": 200, \"msg\": {\"typ\":\"lol\",\"game_uid\": \"b7bbda9b31d0e0a8143b0f57f791f489\", \"display_name\": \"FromUser\"}}";
		try {
			JsonNode jsonNode = JsonObjectMapper.getObjectMapper()
					.readValue(msgStr, JsonNode.class);
			AbstractMessage<?> request = rd.deserialize(jsonNode);
			System.out.println(request);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
