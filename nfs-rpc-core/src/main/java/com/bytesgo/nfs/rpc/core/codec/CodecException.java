/**
 * 
 */
package com.bytesgo.nfs.rpc.core.codec;

import com.bytesgo.nfs.rpc.core.NFSException;

/**
 * @author leeyazhou
 *
 */
public class CodecException extends NFSException {

	private static final long serialVersionUID = 1L;

	public CodecException() {
		super();
	}

	public CodecException(String message) {
		super(message);
	}

	public CodecException(String message, Throwable cause) {
		super(message, cause);
	}

	public CodecException(Throwable cause) {
		super(cause);
	}

	protected CodecException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
