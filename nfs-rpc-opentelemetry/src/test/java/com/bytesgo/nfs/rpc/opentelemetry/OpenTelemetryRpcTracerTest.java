package com.bytesgo.nfs.rpc.opentelemetry;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bytesgo.nfs.rpc.core.tracing.RpcTracer;
import com.bytesgo.nfs.rpc.core.tracing.Span;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.testing.exporter.InMemorySpanExporter;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;

class OpenTelemetryRpcTracerTest {

	private InMemorySpanExporter spanExporter;
	private SdkTracerProvider tracerProvider;
	private OpenTelemetry openTelemetry;

	@BeforeEach
	void setUp() {
		GlobalOpenTelemetry.resetForTest();
		spanExporter = InMemorySpanExporter.create();
		tracerProvider = SdkTracerProvider.builder()
				.addSpanProcessor(SimpleSpanProcessor.create(spanExporter))
				.build();
		openTelemetry = OpenTelemetrySdk.builder()
				.setTracerProvider(tracerProvider)
				.setPropagators(io.opentelemetry.context.propagation.ContextPropagators
						.create(W3CTraceContextPropagator.getInstance()))
				.build();
		GlobalOpenTelemetry.set(openTelemetry);
	}

	@AfterEach
	void tearDown() {
		tracerProvider.close();
		GlobalOpenTelemetry.resetForTest();
	}

	@Test
	void startClientSpan_createsClientKindSpan() {
		RpcTracer tracer = new OpenTelemetryRpcTracer(openTelemetry);

		Span span = tracer.startClientSpan("testMethod", "127.0.0.1:8080");
		span.finish();

		List<io.opentelemetry.sdk.trace.data.SpanData> spans = spanExporter.getFinishedSpanItems();
		assertThat(spans).hasSize(1);
		io.opentelemetry.sdk.trace.data.SpanData data = spans.get(0);
		assertThat(data.getName()).isEqualTo("testMethod");
		assertThat(data.getKind()).isEqualTo(io.opentelemetry.api.trace.SpanKind.CLIENT);
		assertThat(data.getAttributes().get(io.opentelemetry.api.common.AttributeKey.stringKey("rpc.system")))
				.isEqualTo("nfs-rpc");
		assertThat(data.getAttributes().get(io.opentelemetry.api.common.AttributeKey.stringKey("rpc.target")))
				.isEqualTo("127.0.0.1:8080");
	}

	@Test
	void inject_propagatesTraceContextIntoHeaders() {
		RpcTracer tracer = new OpenTelemetryRpcTracer(openTelemetry);

		Span clientSpan = tracer.startClientSpan("testMethod", "127.0.0.1:8080");
		Map<String, String> headers = new HashMap<>();
		tracer.inject(clientSpan, headers);
		clientSpan.finish();

		assertThat(headers).containsKey("traceparent");
		assertThat(headers.get("traceparent")).startsWith("00-");
	}

	@Test
	void startServerSpan_extractsParentFromHeaders() {
		RpcTracer tracer = new OpenTelemetryRpcTracer(openTelemetry);

		Span clientSpan = tracer.startClientSpan("clientMethod", "127.0.0.1:8080");
		Map<String, String> headers = new HashMap<>();
		tracer.inject(clientSpan, headers);
		clientSpan.finish();

		Span serverSpan = tracer.startServerSpan("serverMethod", "local", headers);
		serverSpan.finish();

		List<io.opentelemetry.sdk.trace.data.SpanData> spans = spanExporter.getFinishedSpanItems();
		assertThat(spans).hasSize(2);

		io.opentelemetry.sdk.trace.data.SpanData clientData = spans.stream()
				.filter(s -> s.getKind() == io.opentelemetry.api.trace.SpanKind.CLIENT)
				.findFirst()
				.orElseThrow(() -> new AssertionError("Client span not found"));
		io.opentelemetry.sdk.trace.data.SpanData serverData = spans.stream()
				.filter(s -> s.getKind() == io.opentelemetry.api.trace.SpanKind.SERVER)
				.findFirst()
				.orElseThrow(() -> new AssertionError("Server span not found"));

		assertThat(serverData.getTraceId()).isEqualTo(clientData.getTraceId());
		assertThat(serverData.getParentSpanId()).isEqualTo(clientData.getSpanId());
	}

	@Test
	void setError_recordsExceptionAndErrorStatus() {
		RpcTracer tracer = new OpenTelemetryRpcTracer(openTelemetry);

		Span span = tracer.startClientSpan("testMethod", "127.0.0.1:8080");
		span.setError(new RuntimeException("test error"));
		span.finish();

		List<io.opentelemetry.sdk.trace.data.SpanData> spans = spanExporter.getFinishedSpanItems();
		assertThat(spans).hasSize(1);
		io.opentelemetry.sdk.trace.data.SpanData data = spans.get(0);
		assertThat(data.getStatus().getStatusCode()).isEqualTo(io.opentelemetry.api.trace.StatusCode.ERROR);
		assertThat(data.getEvents()).hasSize(1);
	}

	@Test
	void inject_nullHeaders_doesNotCrash() {
		RpcTracer tracer = new OpenTelemetryRpcTracer(openTelemetry);

		Span span = tracer.startClientSpan("testMethod", "127.0.0.1:8080");
		tracer.inject(span, null);
		span.finish();

		assertThat(spanExporter.getFinishedSpanItems()).hasSize(1);
	}

	@Test
	void startServerSpan_nullHeaders_createsRootSpan() {
		RpcTracer tracer = new OpenTelemetryRpcTracer(openTelemetry);

		Span span = tracer.startServerSpan("serverMethod", "local", null);
		span.finish();

		List<io.opentelemetry.sdk.trace.data.SpanData> spans = spanExporter.getFinishedSpanItems();
		assertThat(spans).hasSize(1);
		io.opentelemetry.sdk.trace.data.SpanData data = spans.get(0);
		assertThat(data.getKind()).isEqualTo(io.opentelemetry.api.trace.SpanKind.SERVER);
		assertThat(data.getParentSpanId()).isEqualTo(io.opentelemetry.api.trace.SpanId.getInvalid());
	}
}
