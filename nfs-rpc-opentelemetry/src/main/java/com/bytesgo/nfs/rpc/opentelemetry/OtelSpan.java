package com.bytesgo.nfs.rpc.opentelemetry;

import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapSetter;

/**
 * Span wrapper backed by OpenTelemetry.
 * <p>
 * Each instance holds an OTel {@link io.opentelemetry.api.trace.Span} and the
 * {@link Scope} that makes it the current span. Calling {@link #finish()}
 * ends the span and closes the scope.
 *
 * @author nfs-rpc
 */
class OtelSpan implements com.bytesgo.nfs.rpc.core.tracing.Span {

	private final io.opentelemetry.api.trace.Span otelSpan;
	private final Scope scope;

	OtelSpan(io.opentelemetry.api.trace.Span otelSpan, Scope scope) {
		this.otelSpan = otelSpan;
		this.scope = scope;
	}

	io.opentelemetry.api.trace.Span getOtelSpan() {
		return otelSpan;
	}

	@Override
	public void setTag(String key, String value) {
		otelSpan.setAttribute(key, value);
	}

	@Override
	public void setError(Throwable t) {
		otelSpan.recordException(t);
		otelSpan.setStatus(StatusCode.ERROR, t.getMessage());
	}

	@Override
	public void finish() {
		otelSpan.end();
		scope.close();
	}

	/**
	 * TextMapSetter that writes into a {@code Map<String, String>}.
	 */
	static final TextMapSetter<java.util.Map<String, String>> MAP_SETTER = new TextMapSetter<java.util.Map<String, String>>() {
		@Override
		public void set(java.util.Map<String, String> carrier, String key, String value) {
			carrier.put(key, value);
		}
	};

	/**
	 * TextMapGetter that reads from a {@code Map<String, String>}.
	 */
	static final TextMapGetter<java.util.Map<String, String>> MAP_GETTER = new TextMapGetter<java.util.Map<String, String>>() {
		@Override
		public Iterable<String> keys(java.util.Map<String, String> carrier) {
			if (carrier == null) {
				return java.util.Collections.emptyList();
			}
			return carrier.keySet();
		}

		@Override
		public String get(java.util.Map<String, String> carrier, String key) {
			if (carrier == null) {
				return null;
			}
			return carrier.get(key);
		}
	};
}
