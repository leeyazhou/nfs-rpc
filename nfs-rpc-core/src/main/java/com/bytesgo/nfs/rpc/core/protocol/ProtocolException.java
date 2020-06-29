/**
 * 
 */
package com.bytesgo.nfs.rpc.core.protocol;

import com.bytesgo.nfs.rpc.core.NFSException;

/**
 * @author leeyazhou
 *
 */
public class ProtocolException extends NFSException {

	private static final long serialVersionUID = 1L;

	public ProtocolException() {
		super();
	}

	public ProtocolException(String message) {
		super(message);
	}

	public ProtocolException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProtocolException(Throwable cause) {
		super(cause);
	}

	protected ProtocolException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
