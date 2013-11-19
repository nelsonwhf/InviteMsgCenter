/**
 * 
 */
package com.duowan.gamebox.msgcenter.plugin.impl;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import com.duowan.common.loadbalance.ProcessException;
import com.duowan.common.loadbalance.Processor;
import com.duowan.webdb.gateway.service.ConsistencyLevelType;
import com.duowan.webdb.gateway.service.ServiceException;
import com.duowan.webdb.gateway.service.WebdbGatewayService.Client;

/**
 * @author zhangtao.robin
 * 
 */
public class GetChannelShortIdProcessor implements Processor<String, String> {
	private String host;
	private int port;
	private int timeout = 3000;

	@Override
	public String process(String request) throws ProcessException {
		TTransport tTrans = new TFramedTransport(new TSocket(host, port, timeout));
		try {
			tTrans.open();
			TProtocol tProtocol = new TBinaryProtocol(tTrans);
			Client client = new Client(tProtocol);
			return client.get_session_asid(request, ConsistencyLevelType.NORMAL);
		} catch (TException e) {
			throw new ProcessException(e);
		} catch (ServiceException e) {
			throw new ProcessException(e);
		} finally {
			tTrans.close();
		}
	}

	@Override
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GetChannelShortIdProcessor [host=").append(host).append(", port=")
				.append(port).append(", timeout=").append(timeout).append("]");
		return builder.toString();
	}

}
