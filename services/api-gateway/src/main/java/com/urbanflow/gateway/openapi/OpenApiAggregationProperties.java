package com.urbanflow.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "urbanflow.openapi")
public class OpenApiAggregationProperties {

    private List<ServiceDefinition> services = new ArrayList<>();

    public List<ServiceDefinition> getServices() {
        return services;
    }

    public void setServices(List<ServiceDefinition> services) {
        this.services = services;
    }

    public record ServiceDefinition(
            String name,
            String displayName,
            String baseUri
    ) {
    }
}
