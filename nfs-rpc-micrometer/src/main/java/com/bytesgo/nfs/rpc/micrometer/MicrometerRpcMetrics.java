package com.bytesgo.nfs.rpc.micrometer;

import java.util.concurrent.TimeUnit;

import com.bytesgo.nfs.rpc.core.metrics.RpcMetrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;

/**
 * Micrometer-based implementation of {@link RpcMetrics}.
 * <p>
 * Records RPC call counts, durations, and connection lifecycle events using a
 * {@link MeterRegistry}. Users install it via:
 *
 * <pre>{@code
 * RpcMetricsHolder.set(new MicrometerRpcMetrics(meterRegistry));
 * }</pre>
 *
 * <h3>Metrics emitted</h3>
 * <ul>
 *   <li>{@code rpc.calls} (Counter) — tags: {@code method}, {@code server}, {@code result}</li>
 *   <li>{@code rpc.duration} (Timer) — tags: {@code method}, {@code server}, {@code result}</li>
 *   <li>{@code rpc.connections} (Counter) — tags: {@code server}, {@code state}</li>
 * </ul>
 *
 * @author nfs-rpc
 */
public class MicrometerRpcMetrics implements RpcMetrics {

	private static final String CALLS_METRIC = "rpc.calls";
	private static final String DURATION_METRIC = "rpc.duration";
	private static final String CONNECTIONS_METRIC = "rpc.connections";

	private final MeterRegistry registry;

	public MicrometerRpcMetrics(MeterRegistry registry) {
		this.registry = registry;
	}

	@Override
	public void recordCall(String method, String serverAddress, long durationNanos, boolean success) {
		Tags tags = Tags.of("method", method == null ? "unknown" : method)
				.and("server", serverAddress == null ? "unknown" : serverAddress)
				.and("result", success ? "success" : "failure");

		registry.counter(CALLS_METRIC, tags).increment();
		Timer.builder(DURATION_METRIC)
				.tags(tags)
				.register(registry)
				.record(durationNanos, TimeUnit.NANOSECONDS);
	}

	@Override
	public void recordConnection(String serverAddress, boolean connected) {
		Tags tags = Tags.of("server", serverAddress == null ? "unknown" : serverAddress)
				.and("state", connected ? "connected" : "disconnected");
		registry.counter(CONNECTIONS_METRIC, tags).increment();
	}
}
