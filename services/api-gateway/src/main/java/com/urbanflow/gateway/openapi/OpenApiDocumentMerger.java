package com.urbanflow.gateway.openapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class OpenApiDocumentMerger {

    private final ObjectMapper objectMapper;

    public OpenApiDocumentMerger(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ObjectNode emptyDocument() {
        ObjectNode document = objectMapper.createObjectNode();
        document.put("openapi", "3.0.1");
        document.set("info", objectMapper.createObjectNode()
                .put("title", "UrbanFlow Platform API")
                .put("description", "Unified OpenAPI document aggregated from all UrbanFlow microservices.")
                .put("version", "1.0.0"));
        document.set("paths", objectMapper.createObjectNode());
        document.set("tags", objectMapper.createArrayNode());
        document.set("components", objectMapper.createObjectNode()
                .set("schemas", objectMapper.createObjectNode())
                .set("securitySchemes", objectMapper.createObjectNode()));
        return document;
    }

    public ObjectNode merge(ObjectNode target, OpenApiServiceDocument source) {
        mergePaths(target, source);
        mergeTags(target, source);
        mergeComponents(target, source);
        return target;
    }

    public ObjectNode finalizeDocument(ObjectNode document) {
        ObjectNode securitySchemes = ensureObjectNode(ensureObjectNode(document, "components"), "securitySchemes");
        if (!securitySchemes.has("bearerAuth")) {
            securitySchemes.set("bearerAuth", objectMapper.createObjectNode()
                    .put("type", "http")
                    .put("scheme", "bearer")
                    .put("bearerFormat", "JWT")
                    .put("description", "JWT access token issued by auth-service"));
        }

        ArrayNode security = objectMapper.createArrayNode();
        security.add(objectMapper.createObjectNode().set("bearerAuth", objectMapper.createArrayNode()));
        document.set("security", security);

        ArrayNode servers = objectMapper.createArrayNode();
        servers.add(objectMapper.createObjectNode()
                .put("url", "/")
                .put("description", "UrbanFlow API Gateway"));
        document.set("servers", servers);

        return document;
    }

    private void mergePaths(ObjectNode target, OpenApiServiceDocument source) {
        ObjectNode targetPaths = (ObjectNode) target.get("paths");
        JsonNode sourcePaths = source.document().get("paths");
        if (sourcePaths == null || !sourcePaths.isObject()) {
            return;
        }

        Iterator<String> fieldNames = sourcePaths.fieldNames();
        while (fieldNames.hasNext()) {
            String path = fieldNames.next();
            targetPaths.set(path, sourcePaths.get(path));
        }
    }

    private void mergeTags(ObjectNode target, OpenApiServiceDocument source) {
        ArrayNode targetTags = ensureArrayNode(target, "tags");
        Set<String> existing = new LinkedHashSet<>();
        targetTags.forEach(tag -> existing.add(tag.get("name").asText()));

        String prefix = source.displayName();
        JsonNode sourceTags = source.document().get("tags");
        if (sourceTags != null && sourceTags.isArray()) {
            sourceTags.forEach(tag -> {
                String name = prefix + " — " + tag.get("name").asText();
                if (existing.add(name)) {
                    ObjectNode mergedTag = objectMapper.createObjectNode();
                    mergedTag.put("name", name);
                    if (tag.has("description")) {
                        mergedTag.put("description", tag.get("description").asText());
                    }
                    targetTags.add(mergedTag);
                }
            });
        }
    }

    private void mergeComponents(ObjectNode target, OpenApiServiceDocument source) {
        JsonNode sourceComponents = source.document().get("components");
        if (sourceComponents == null || !sourceComponents.isObject()) {
            return;
        }

        ObjectNode targetComponents = ensureObjectNode(target, "components");
        mergeComponentSection(targetComponents, sourceComponents, "schemas", source.name());
        mergeComponentSection(targetComponents, sourceComponents, "securitySchemes", source.name());
    }

    private void mergeComponentSection(
            ObjectNode targetComponents,
            JsonNode sourceComponents,
            String section,
            String serviceName
    ) {
        JsonNode sourceSection = sourceComponents.get(section);
        if (sourceSection == null || !sourceSection.isObject()) {
            return;
        }

        ObjectNode targetSection = ensureObjectNode(targetComponents, section);
        Iterator<String> names = sourceSection.fieldNames();
        while (names.hasNext()) {
            String name = names.next();
            String targetName = targetSection.has(name)
                    ? serviceName + "_" + name
                    : name;
            targetSection.set(targetName, sourceSection.get(name));
        }
    }

    private ObjectNode ensureObjectNode(ObjectNode parent, String field) {
        JsonNode node = parent.get(field);
        if (node instanceof ObjectNode objectNode) {
            return objectNode;
        }
        ObjectNode created = objectMapper.createObjectNode();
        parent.set(field, created);
        return created;
    }

    private ArrayNode ensureArrayNode(ObjectNode parent, String field) {
        JsonNode node = parent.get(field);
        if (node instanceof ArrayNode arrayNode) {
            return arrayNode;
        }
        ArrayNode created = objectMapper.createArrayNode();
        parent.set(field, created);
        return created;
    }
}
