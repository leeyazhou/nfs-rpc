package com.bytesgo.nfs.rpc.micrometer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

class MicrometerRpcMetricsTest {

	@Test
	void recordCall_createsCounterAndTimerWithTags() {
		SimpleMeterRegistry registry = new SimpleMeterRegistry();
		MicrometerRpcMetrics metrics = new MicrometerRpcMetrics(registry);

		metrics.recordCall("echo", "127.0.0.1:8080", 5_000_000L, true);
		metrics.recordCall("echo", "127.0.0.1:8080", 3_000_000L, true);
		metrics.recordCall("echo", "127.0.0.1:8080", 1_000_000L, false);

		assertThat(registry.counter("rpc.calls",
				"method", "echo",
				"server", "127.0.0.1:8080",
				"result", "success").count()).isEqualTo(2.0);

		assertThat(registry.counter("rpc.calls",
				"method", "echo",
				"server", "127.0.0.1:8080",
				"result", "failure").count()).isEqualTo(1.0);

		assertThat(registry.timer("rpc.duration",
				"method", "echo",
				"server", "127.0.0.1:8080",
				"result", "success").count()).isEqualTo(2);
	}

	@Test
	void recordCall_nullMethodOrServer_doesNotCrash() {
		SimpleMeterRegistry registry = new SimpleMeterRegistry();
		MicrometerRpcMetrics metrics = new MicrometerRpcMetrics(registry);

		metrics.recordCall(null, null, 1_000_000L, true);

		assertThat(registry.counter("rpc.calls",
				"method", "unknown",
				"server", "unknown",
				"result", "success").count()).isEqualTo(1.0);
	}

	@Test
	void recordConnection_createsCounterWithStateTag() {
		SimpleMeterRegistry registry = new SimpleMeterRegistry();
		MicrometerRpcMetrics metrics = new MicrometerRpcMetrics(registry);

		metrics.recordConnection("127.0.0.1:8080", true);
		metrics.recordConnection("127.0.0.1:8080", true);
		metrics.recordConnection("127.0.0.1:8080", false);

		assertThat(registry.counter("rpc.connections",
				"server", "127.0.0.1:8080",
				"state", "connected").count()).isEqualTo(2.0);

		assertThat(registry.counter("rpc.connections",
				"server", "127.0.0.1:8080",
				"state", "disconnected").count()).isEqualTo(1.0);
	}

	@Test
	void recordCall_timerRecordsNanosAccurately() {
		SimpleMeterRegistry registry = new SimpleMeterRegistry();
		MicrometerRpcMetrics metrics = new MicrometerRpcMetrics(registry);

		long durationNanos = TimeUnit.MILLISECONDS.toNanos(100);
		metrics.recordCall("ping", "10.0.0.1:9090", durationNanos, true);

		double recordedNanos = registry.timer("rpc.duration",
				"method", "ping",
				"server", "10.0.0.1:9090",
				"result", "success")
				.totalTime(TimeUnit.NANOSECONDS);

		assertThat(recordedNanos).isEqualTo(durationNanos);
	}
}
