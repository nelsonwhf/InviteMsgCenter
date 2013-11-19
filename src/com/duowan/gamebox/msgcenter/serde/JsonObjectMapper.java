/**
 * 
 */
package com.duowan.gamebox.msgcenter.serde;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author zhangtao.robin
 * 
 */
public class JsonObjectMapper {

	// disable auto close source stream
	// 2013-11-08 disable FAIL_ON_UNKNOWN_PROPERTIES
	private static final ObjectMapper objectMapper = new ObjectMapper(
			new JsonFactory().disable(JsonParser.Feature.AUTO_CLOSE_SOURCE).disable(
					JsonGenerator.Feature.AUTO_CLOSE_TARGET)).configure(
			DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	private JsonObjectMapper() {
		// none
	}

	public static final ObjectMapper getObjectMapper() {
		return objectMapper;
	}
}
