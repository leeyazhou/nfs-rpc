package com.bytesgo.nfs.rpc.core.exception;

/**
 * Thrown when deserializing the response object fails.
 *
 * @author leeyazhou
 */
public class RpcDecodeException extends NFSException {

	private static final long serialVersionUID = 1L;

	public RpcDecodeException(String message) {
		super(message);
	}

	public RpcDecodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public RpcDecodeException(Throwable cause) {
		super(cause);
	}

}
