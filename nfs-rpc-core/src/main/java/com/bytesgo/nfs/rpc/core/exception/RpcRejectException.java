package com.bytesgo.nfs.rpc.core.exception;

/**
 * Thrown when sending bytes size exceeds the configured limit threshold.
 *
 * @author leeyazhou
 */
public class RpcRejectException extends NFSException {

	private static final long serialVersionUID = 1L;

	private final long sendingBytesSize;

	private final long threshold;

	public RpcRejectException(long sendingBytesSize, long threshold) {
		super("sending bytes size exceed threshold,size: " + sendingBytesSize + ", threshold: " + threshold);
		this.sendingBytesSize = sendingBytesSize;
		this.threshold = threshold;
	}

	public RpcRejectException(String message) {
		super(message);
		this.sendingBytesSize = 0;
		this.threshold = 0;
	}

	public long getSendingBytesSize() {
		return sendingBytesSize;
	}

	public long getThreshold() {
		return threshold;
	}

}
