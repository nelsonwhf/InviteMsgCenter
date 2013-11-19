/**
 * 
 */
package com.duowan.gamebox.msgcenter.serde;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duowan.gamebox.msgcenter.msg.AbstractMessage;

/**
 * @author zhangtao.robin
 * 
 */
public class MessageSerializer {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	private ObjectMapper mapper = JsonObjectMapper.getObjectMapper();

	public void serialize(OutputStream out, AbstractMessage<?> msg) throws JsonGenerationException,
			JsonMappingException, IOException {
		writeMessage(out, msg, false);
	}

	public void serializeWithLineBreak(OutputStream out, AbstractMessage<?> msg)
			throws IOException {
		writeMessage(out, msg, true);
	}

	private void writeMessage(OutputStream out, AbstractMessage<?> msg, boolean lineBreak)
			throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream(1024);
		mapper.writeValue(bout, msg);
		if (lineBreak) {
			bout.write(AbstractMessage.LINE_SPLITTER_BYTES);
		}
		// 2013-11-12 synchronized because of multiple threads will call it and
		// the OutputStream has buffer
		synchronized (out) {
			out.write(bout.toByteArray());
			out.flush();
		}
		if (log.isDebugEnabled()) {
			log.debug("Write message: " + new String(bout.toByteArray(), "UTF-8"));
		}
	}

}
