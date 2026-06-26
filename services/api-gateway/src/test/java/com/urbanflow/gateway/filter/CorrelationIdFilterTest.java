package com.urbanflow.gateway.filter;

import com.urbanflow.common.constant.HttpHeaderConstants;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CorrelationIdFilterTest {

    private final CorrelationIdFilter filter = new CorrelationIdFilter();

    @Test
    void shouldGenerateRequestAndCorrelationIdsWhenMissing() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/traffic").build()
        );
        GatewayFilterChain chain = mock(GatewayFilterChain.class);
        when(chain.filter(any())).thenReturn(Mono.empty());

        filter.filter(exchange, chain).block();

        String requestId = exchange.getResponse().getHeaders().getFirst(HttpHeaderConstants.REQUEST_ID);
        String correlationId = exchange.getResponse().getHeaders().getFirst(HttpHeaderConstants.CORRELATION_ID);

        assertNotNull(requestId);
        assertNotNull(correlationId);
        assertEquals(requestId, correlationId);
    }

    @Test
    void shouldPreserveProvidedHeaders() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/traffic")
                        .header(HttpHeaderConstants.REQUEST_ID, "req-123")
                        .header(HttpHeaderConstants.CORRELATION_ID, "corr-456")
                        .build()
        );
        GatewayFilterChain chain = mock(GatewayFilterChain.class);
        when(chain.filter(any())).thenReturn(Mono.empty());

        filter.filter(exchange, chain).block();

        assertEquals("req-123", exchange.getResponse().getHeaders().getFirst(HttpHeaderConstants.REQUEST_ID));
        assertEquals("corr-456", exchange.getResponse().getHeaders().getFirst(HttpHeaderConstants.CORRELATION_ID));
    }
}
