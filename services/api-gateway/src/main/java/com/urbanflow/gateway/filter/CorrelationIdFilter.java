package com.urbanflow.gateway.filter;

import com.urbanflow.common.constant.HttpHeaderConstants;
import com.urbanflow.common.util.IdGenerator;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String requestId = request.getHeaders().getFirst(HttpHeaderConstants.REQUEST_ID);
        if (requestId == null || requestId.isBlank()) {
            requestId = IdGenerator.newCorrelationId();
        }

        String correlationId = request.getHeaders().getFirst(HttpHeaderConstants.CORRELATION_ID);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = requestId;
        }

        ServerHttpRequest mutatedRequest = request.mutate()
                .header(HttpHeaderConstants.REQUEST_ID, requestId)
                .header(HttpHeaderConstants.CORRELATION_ID, correlationId)
                .build();

        exchange.getResponse().getHeaders().add(HttpHeaderConstants.REQUEST_ID, requestId);
        exchange.getResponse().getHeaders().add(HttpHeaderConstants.CORRELATION_ID, correlationId);

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
