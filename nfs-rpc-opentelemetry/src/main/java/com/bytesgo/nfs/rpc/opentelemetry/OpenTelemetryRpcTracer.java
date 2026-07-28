package com.bytesgo.nfs.rpc.opentelemetry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.bytesgo.nfs.rpc.core.tracing.RpcTracer;
import com.bytesgo.nfs.rpc.core.tracing.Span;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;

/**
 * OpenTelemetry implementation of the {@link RpcTracer} SPI.
 * <p>
 * Uses W3C Trace Context propagation (traceparent / tracestate headers) to
 * propagate trace context across RPC calls.
 * <p>
 * Usage:
 * <pre>
 * OpenTelemetry openTelemetry = GlobalOpenTelemetry.get();
 * RpcTracerHolder.set(new OpenTelemetryRpcTracer(openTelemetry));
 * </pre>
 *
 * @author nfs-rpc
 */
public class OpenTelemetryRpcTracer implements RpcTracer {

	private final Tracer tracer;
	private final OpenTelemetry openTelemetry;

	/**
	 * Create a tracer that uses the global {@link OpenTelemetry} instance.
	 */
	public OpenTelemetryRpcTracer() {
		this(GlobalOpenTelemetry.get());
	}

	/**
	 * Create a tracer with a custom {@link OpenTelemetry} instance.
	 *
	 * @param openTelemetry the OpenTelemetry instance to use
	 */
	public OpenTelemetryRpcTracer(OpenTelemetry openTelemetry) {
		this.openTelemetry = openTelemetry;
		this.tracer = openTelemetry.getTracer("nfs-rpc");
	}

	@Override
	public Span startClientSpan(String operationName, String remoteAddress) {
		SpanBuilder builder = tracer.spanBuilder(operationName)
				.setSpanKind(SpanKind.CLIENT)
				.setAttribute("rpc.system", "nfs-rpc")
				.setAttribute("rpc.target", remoteAddress);
		io.opentelemetry.api.trace.Span otelSpan = builder.startSpan();
		Scope scope = otelSpan.makeCurrent();
		return new OtelSpan(otelSpan, scope);
	}

	@Override
	public void inject(Span span, Map<String, String> headers) {
		if (headers == null || !(span instanceof OtelSpan)) {
			return;
		}
		OtelSpan otelSpan = (OtelSpan) span;
		Context context = Context.current().with(otelSpan.getOtelSpan());
		W3CTraceContextPropagator.getInstance().inject(context, headers, OtelSpan.MAP_SETTER);
	}

	@Override
	public Span startServerSpan(String operationName, String remoteAddress, Map<String, String> headers) {
		Map<String, String> carrier = headers != null ? headers : Collections.<String, String>emptyMap();
		Context extractedContext = W3CTraceContextPropagator.getInstance()
				.extract(Context.current(), carrier, OtelSpan.MAP_GETTER);
		SpanBuilder builder = tracer.spanBuilder(operationName)
				.setSpanKind(SpanKind.SERVER)
				.setParent(extractedContext)
				.setAttribute("rpc.system", "nfs-rpc")
				.setAttribute("rpc.target", remoteAddress);
		io.opentelemetry.api.trace.Span otelSpan = builder.startSpan();
		Scope scope = otelSpan.makeCurrent();
		return new OtelSpan(otelSpan, scope);
	}
}
