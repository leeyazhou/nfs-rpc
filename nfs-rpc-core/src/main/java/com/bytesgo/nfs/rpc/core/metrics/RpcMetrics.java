package com.bytesgo.nfs.rpc.core.metrics;

/**
 * SPI interface for recording RPC observability metrics.
 * <p>
 * The default implementation is {@link #NOOP}, which discards all data.
 * Users can plug in their own implementation (e.g. Micrometer-based) via
 * {@link RpcMetricsHolder#set(RpcMetrics)}.
 *
 * @author nfs-rpc
 */
public interface RpcMetrics {

	/** No-op implementation that discards all metrics. */
	RpcMetrics NOOP = new RpcMetrics() {
	};

	/**
	 * Record a completed RPC call.
	 *
	 * @param method        the method name (e.g. {@code "serviceName.methodName"})
	 * @param serverAddress the server address (e.g. {@code "127.0.0.1:8080"}) or {@code "local"} for server-side
	 * @param durationNanos the call duration in nanoseconds
	 * @param success       {@code true} if the call completed without error
	 */
	default void recordCall(String method, String serverAddress, long durationNanos, boolean success) {
	}

	/**
	 * Record a connection lifecycle event.
	 *
	 * @param serverAddress the server address
	 * @param connected     {@code true} when a connection is established, {@code false} when removed
	 */
	default void recordConnection(String serverAddress, boolean connected) {
	}
}
