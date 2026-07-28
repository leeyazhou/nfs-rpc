package com.bytesgo.nfs.rpc.core.exception;

/**
 * Thrown when the remote server returns an error/exception during RPC invocation.
 *
 * @author leeyazhou
 */
public class RpcRemoteException extends NFSException {

	private static final long serialVersionUID = 1L;

	private final String serverAddress;

	private final int requestId;

	public RpcRemoteException(String message, Throwable cause, String serverAddress, int requestId) {
		super(message, cause);
		this.serverAddress = serverAddress;
		this.requestId = requestId;
	}

	public RpcRemoteException(String message) {
		super(message);
		this.serverAddress = null;
		this.requestId = 0;
	}

	public RpcRemoteException(String message, Throwable cause) {
		super(message, cause);
		this.serverAddress = null;
		this.requestId = 0;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public int getRequestId() {
		return requestId;
	}

}
