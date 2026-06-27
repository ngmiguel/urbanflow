package com.urbanflow.gateway.openapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class OpenApiAggregationService {

    private static final Logger log = LoggerFactory.getLogger(OpenApiAggregationService.class);
    private static final Duration FETCH_TIMEOUT = Duration.ofSeconds(5);

    private final OpenApiAggregationProperties properties;
    private final OpenApiDocumentMerger documentMerger;
    private final WebClient webClient;

    public OpenApiAggregationService(
            OpenApiAggregationProperties properties,
            OpenApiDocumentMerger documentMerger,
            WebClient.Builder webClientBuilder
    ) {
        this.properties = properties;
        this.documentMerger = documentMerger;
        this.webClient = webClientBuilder.build();
    }

    public Mono<JsonNode> aggregate() {
        if (properties.getServices().isEmpty()) {
            return Mono.just(documentMerger.finalizeDocument(documentMerger.emptyDocument()));
        }

        return Flux.fromIterable(properties.getServices())
                .flatMap(this::fetchServiceDocument)
                .reduce(documentMerger.emptyDocument(), documentMerger::merge)
                .map(documentMerger::finalizeDocument);
    }

    private Mono<OpenApiServiceDocument> fetchServiceDocument(OpenApiAggregationProperties.ServiceDefinition service) {
        String url = service.baseUri().replaceAll("/$", "") + "/v3/api-docs";

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .timeout(FETCH_TIMEOUT)
                .map(body -> new OpenApiServiceDocument(service.name(), service.displayName(), body))
                .onErrorResume(error -> {
                    log.warn("Unable to fetch OpenAPI document from {} ({}): {}",
                            service.displayName(), url, error.getMessage());
                    return Mono.empty();
                });
    }
}
