package com.bytesgo.nfs.rpc.core.metrics;

/**
 * Static holder for the active {@link RpcMetrics} instance.
 * <p>
 * Defaults to {@link RpcMetrics#NOOP}. Call {@link #set(RpcMetrics)} once
 * during application bootstrap to install a custom implementation.
 *
 * @author nfs-rpc
 */
public final class RpcMetricsHolder {

	private static volatile RpcMetrics metrics = RpcMetrics.NOOP;

	private RpcMetricsHolder() {
	}

	/**
	 * @return the active metrics instance (never {@code null})
	 */
	public static RpcMetrics get() {
		return metrics;
	}

	/**
	 * Install a custom metrics implementation. Passing {@code null} resets to
	 * {@link RpcMetrics#NOOP}.
	 *
	 * @param metrics the implementation to use, or {@code null} for no-op
	 */
	public static void set(RpcMetrics metrics) {
		RpcMetricsHolder.metrics = (metrics == null) ? RpcMetrics.NOOP : metrics;
	}
}
