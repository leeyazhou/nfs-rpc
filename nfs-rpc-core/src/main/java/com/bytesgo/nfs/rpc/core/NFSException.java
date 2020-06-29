/**
 * 
 */
package com.bytesgo.nfs.rpc.core;

/**
 * @author leeyazhou
 *
 */
public class NFSException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NFSException() {
		super();
	}

	public NFSException(String message) {
		super(message);
	}

	public NFSException(String message, Throwable cause) {
		super(message, cause);
	}

	public NFSException(Throwable cause) {
		super(cause);
	}

	protected NFSException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
