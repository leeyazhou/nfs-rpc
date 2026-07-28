package com.bytesgo.nfs.rpc.core.tracing;

import java.util.Map;

/**
 * SPI interface for distributed tracing.
 * <p>
 * The default implementation {@link #NOOP} creates no-op spans and performs no
 * context propagation. Users can plug in their own implementation (e.g.
 * OpenTelemetry-based) via {@link RpcTracerHolder#set(RpcTracer)}.
 * <p>
 * Typical flow on the <strong>client</strong> side:
 * <pre>
 * Span span = tracer.startClientSpan("serviceName.methodName", "10.0.0.1:8080");
 * Map&lt;String, String&gt; headers = new HashMap&lt;&gt;();
 * tracer.inject(span, headers);
 * request.setHeaders(headers);
 * // ... send request, receive response ...
 * span.finish();  // in finally
 * </pre>
 * <p>
 * Typical flow on the <strong>server</strong> side:
 * <pre>
 * Span span = tracer.startServerSpan("serviceName.methodName", "local", request.getHeaders());
 * // ... handle request ...
 * span.finish();  // in finally
 * </pre>
 *
 * @author nfs-rpc
 */
public interface RpcTracer {

	/** No-op tracer that creates no-op spans and performs no propagation. */
	RpcTracer NOOP = new RpcTracer() {
	};

	/**
	 * Start a span for an outgoing client request.
	 *
	 * @param operationName the operation name (e.g. {@code "serviceName.methodName"})
	 * @param remoteAddress the remote server address (e.g. {@code "10.0.0.1:8080"})
	 * @return a {@link Span} that must be finished after the call completes
	 */
	default Span startClientSpan(String operationName, String remoteAddress) {
		return Span.NOOP;
	}

	/**
	 * Inject trace context from the span into the headers map for network
	 * propagation.
	 *
	 * @param span    the span created by {@link #startClientSpan}
	 * @param headers the headers map to populate (never {@code null})
	 */
	default void inject(Span span, Map<String, String> headers) {
	}

	/**
	 * Start a span for an incoming server request, extracting the parent
	 * context from the headers propagated by the client.
	 *
	 * @param operationName the operation name (e.g. {@code "serviceName.methodName"})
	 * @param remoteAddress the server address (e.g. {@code "local"})
	 * @param headers       the headers map from the request (may be {@code null})
	 * @return a {@link Span} that must be finished after handling completes
	 */
	default Span startServerSpan(String operationName, String remoteAddress, Map<String, String> headers) {
		return Span.NOOP;
	}
}
