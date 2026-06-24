package com.urbanflow.common.constant;

/**
 * Standard HTTP headers propagated across the platform for observability.
 */
public final class HttpHeaderConstants {

    public static final String REQUEST_ID = "X-Request-Id";
    public static final String CORRELATION_ID = "X-Correlation-Id";
    public static final String IDEMPOTENCY_KEY = "X-Idempotency-Key";

    private HttpHeaderConstants() {
    }
}
