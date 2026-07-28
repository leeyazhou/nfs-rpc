package com.bytesgo.nfs.rpc.core.tracing;

/**
 * SPI interface for a tracing span.
 * <p>
 * The default implementation {@link #NOOP} discards all operations. Users can
 * plug in their own implementation (e.g. OpenTelemetry-based) via
 * {@link RpcTracerHolder#set(RpcTracer)}.
 *
 * @author nfs-rpc
 */
public interface Span {

	/** No-op span that discards all operations. */
	Span NOOP = new Span() {
	};

	/**
	 * Set a string tag on the span.
	 *
	 * @param key   the tag key
	 * @param value the tag value
	 */
	default void setTag(String key, String value) {
	}

	/**
	 * Mark the span as failed with the given error.
	 *
	 * @param t the error that occurred
	 */
	default void setError(Throwable t) {
	}

	/**
	 * Finish the span. Must be called exactly once.
	 */
	default void finish() {
	}
}
