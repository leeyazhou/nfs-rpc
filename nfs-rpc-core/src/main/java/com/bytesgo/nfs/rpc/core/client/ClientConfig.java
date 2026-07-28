package com.bytesgo.nfs.rpc.core.client;

import com.bytesgo.nfs.rpc.codec.Codecs;
import com.bytesgo.nfs.rpc.core.exception.NFSException;
import com.bytesgo.nfs.rpc.core.protocol.RPCProtocol;

/**
 * @author leeyazhou
 */
public class ClientConfig {

	private static final int MIN_PORT = 1;
	private static final int MAX_PORT = 65535;
	private static final int DEFAULT_CONNECT_TIMEOUT = 3000;

	private String host;
	private int port;
	private int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
	private int clientNums = 1;
	private int codecType = Codecs.HESSIAN_CODEC;
	private int protocolType = RPCProtocol.TYPE;

	public String getHost() {
		return host;
	}

	public ClientConfig setHost(String host) {
		this.host = host;
		return this;
	}

	public int getPort() {
		return port;
	}

	public ClientConfig setPort(int port) {
		this.port = port;
		return this;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public ClientConfig setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
		return this;
	}

	public int getClientNums() {
		return clientNums;
	}

	public ClientConfig setClientNums(int clientNums) {
		this.clientNums = clientNums;
		return this;
	}

	public int getCodecType() {
		return codecType;
	}

	public ClientConfig setCodecType(int codecType) {
		this.codecType = codecType;
		return this;
	}

	public int getProtocolType() {
		return protocolType;
	}

	public ClientConfig setProtocolType(int protocolType) {
		this.protocolType = protocolType;
		return this;
	}

	public ClientConfig validate() {
		if (host == null || host.trim().isEmpty()) {
			throw new NFSException("host must not be null or blank");
		}
		if (port < MIN_PORT || port > MAX_PORT) {
			throw new NFSException("Invalid port: " + port + ", must be between " + MIN_PORT + " and " + MAX_PORT);
		}
		if (connectTimeout < 0) {
			throw new NFSException("Invalid connectTimeout: " + connectTimeout + ", must be >= 0");
		}
		if (clientNums < 1) {
			throw new NFSException("Invalid clientNums: " + clientNums + ", must be >= 1");
		}
		return this;
	}

}
