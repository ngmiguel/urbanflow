package com.urbanflow.simulator.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "urbanflow.simulator")
public record SimulatorProperties(
        long tickIntervalMs,
        int defaultDurationSeconds,
        String defaultZoneId,
        double defaultLatitude,
        double defaultLongitude
) {
}
