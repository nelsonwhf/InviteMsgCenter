package com.duowan.gamebox.msgcenter.serialize.impl;

import com.duowan.gamebox.msgcenter.manager.vo.Invitation;
import com.duowan.gamebox.msgcenter.manager.vo.InvitationDetail;
import com.duowan.gamebox.msgcenter.serialize.tools.AvroSerializer;
import com.duowan.logcenter.client.BytesLogClient;
import com.duowan.logcenter.client.BytesLogLevel;
import com.duowan.logcenter.client.BytesLogMessage;

public class BytesSerializeClientRedisQueueImpl {

	private BytesLogClient logClient;

	private AvroSerializer avroSerializer = new AvroSerializer();

	public BytesLogClient getLogClient() {
		return logClient;
	}

	public void setLogClient(BytesLogClient logClient) {
		this.logClient = logClient;
	}

	public AvroSerializer getAvroSerializer() {
		return avroSerializer;
	}

	public void setAvroSerializer(AvroSerializer avroSerializer) {
		this.avroSerializer = avroSerializer;
	}

	public void sendInvitationBytesMessage(String name, Invitation invitation, String serverUrl) {
		byte[] bytes = avroSerializer.invitationSerialize(invitation, serverUrl);
		long timestamp = System.currentTimeMillis();
		BytesLogMessage message = new BytesLogMessage(name, BytesLogLevel.INFO, timestamp, bytes);
		sendBytesLogMessage(message);
	}

	public void sendInvitationDetailBytesMessage(String name,
			InvitationDetail invitationDetail, String serverUrl) {
		byte[] bytes = avroSerializer.invitationDetailSerialize(invitationDetail, serverUrl);
		long timestamp = System.currentTimeMillis();
		BytesLogMessage message = new BytesLogMessage(name, BytesLogLevel.INFO, timestamp, bytes);
		sendBytesLogMessage(message);
	}

	public void sendErrorBytesMessage(String name, String serverUrl) {
		long timestamp = System.currentTimeMillis();
		BytesLogMessage message = new BytesLogMessage(name, BytesLogLevel.ERROR, timestamp, null);
		sendBytesLogMessage(message);
	}

	public void sendBytesLogMessage(BytesLogMessage message) {
		logClient.sendBytesLogMessage(message);
	}

}
