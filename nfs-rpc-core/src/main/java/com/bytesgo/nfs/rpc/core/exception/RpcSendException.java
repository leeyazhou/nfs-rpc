package com.bytesgo.nfs.rpc.core.exception;

/**
 * Thrown when sending a request to the OS send buffer fails.
 *
 * @author leeyazhou
 */
public class RpcSendException extends NFSException {

	private static final long serialVersionUID = 1L;

	public RpcSendException(String message) {
		super(message);
	}

	public RpcSendException(String message, Throwable cause) {
		super(message, cause);
	}

	public RpcSendException(Throwable cause) {
		super(cause);
	}

}
