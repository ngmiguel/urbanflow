package com.urbanflow.gateway.openapi;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class OpenApiAggregationController {

    private final OpenApiAggregationService openApiAggregationService;

    public OpenApiAggregationController(OpenApiAggregationService openApiAggregationService) {
        this.openApiAggregationService = openApiAggregationService;
    }

    @GetMapping(value = "/v3/api-docs/urbanflow", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<JsonNode> unifiedApiDocs() {
        return openApiAggregationService.aggregate();
    }
}
