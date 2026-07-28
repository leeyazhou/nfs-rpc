package com.bytesgo.nfs.rpc.core.tracing;

/**
 * Static holder for the active {@link RpcTracer} instance.
 * <p>
 * Defaults to {@link RpcTracer#NOOP}. Call {@link #set(RpcTracer)} once
 * during application bootstrap to install a custom implementation.
 *
 * @author nfs-rpc
 */
public final class RpcTracerHolder {

	private static volatile RpcTracer tracer = RpcTracer.NOOP;

	private RpcTracerHolder() {
	}

	/**
	 * @return the active tracer instance (never {@code null})
	 */
	public static RpcTracer get() {
		return tracer;
	}

	/**
	 * Install a custom tracer implementation. Passing {@code null} resets to
	 * {@link RpcTracer#NOOP}.
	 *
	 * @param tracer the implementation to use, or {@code null} for no-op
	 */
	public static void set(RpcTracer tracer) {
		RpcTracerHolder.tracer = (tracer == null) ? RpcTracer.NOOP : tracer;
	}
}
