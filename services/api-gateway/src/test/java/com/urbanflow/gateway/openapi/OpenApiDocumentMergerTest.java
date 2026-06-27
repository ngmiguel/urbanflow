package com.urbanflow.gateway.openapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpenApiDocumentMergerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OpenApiDocumentMerger merger = new OpenApiDocumentMerger(objectMapper);

    @Test
    void shouldMergePathsFromMultipleDocuments() throws Exception {
        ObjectNode merged = merger.emptyDocument();
        merger.merge(merged, new OpenApiServiceDocument(
                "auth-service",
                "Auth",
                objectMapper.readTree("""
                        {
                          "paths": {
                            "/api/v1/auth/login": {
                              "post": { "tags": ["Auth"], "summary": "Login" }
                            }
                          },
                          "tags": [{ "name": "Auth" }]
                        }
                        """)
        ));
        merger.merge(merged, new OpenApiServiceDocument(
                "traffic-service",
                "Traffic",
                objectMapper.readTree("""
                        {
                          "paths": {
                            "/api/v1/traffic/segments": {
                              "get": { "tags": ["Traffic"], "summary": "List segments" }
                            }
                          },
                          "tags": [{ "name": "Traffic" }]
                        }
                        """)
        ));
        JsonNode document = merger.finalizeDocument(merged);

        assertTrue(document.get("paths").has("/api/v1/auth/login"));
        assertTrue(document.get("paths").has("/api/v1/traffic/segments"));
        assertEquals("UrbanFlow Platform API", document.get("info").get("title").asText());
        assertTrue(document.get("components").get("securitySchemes").has("bearerAuth"));
    }
}
