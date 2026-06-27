package com.urbanflow.gateway.openapi;

import com.fasterxml.jackson.databind.JsonNode;

public record OpenApiServiceDocument(String name, String displayName, JsonNode document) {
}
