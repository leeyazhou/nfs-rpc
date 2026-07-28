package com.bytesgo.nfs.rpc.core.exception;

/**
 * Thrown when an RPC invocation times out waiting for a response.
 *
 * @author leeyazhou
 */
public class RpcTimeoutException extends NFSException {

	private static final long serialVersionUID = 1L;

	private final int timeout;

	private final String serverAddress;

	private final int requestId;

	public RpcTimeoutException(int timeout, String serverAddress, int requestId) {
		super("receive response timeout(" + timeout + " ms),server is: " + serverAddress + " request id is:" + requestId);
		this.timeout = timeout;
		this.serverAddress = serverAddress;
		this.requestId = requestId;
	}

	public RpcTimeoutException(String message) {
		super(message);
		this.timeout = 0;
		this.serverAddress = null;
		this.requestId = 0;
	}

	public RpcTimeoutException(String message, Throwable cause) {
		super(message, cause);
		this.timeout = 0;
		this.serverAddress = null;
		this.requestId = 0;
	}

	public int getTimeout() {
		return timeout;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public int getRequestId() {
		return requestId;
	}

}
