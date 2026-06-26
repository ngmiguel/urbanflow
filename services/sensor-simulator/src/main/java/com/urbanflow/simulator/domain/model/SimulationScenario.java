package com.urbanflow.simulator.domain.model;

import com.urbanflow.common.exception.ResourceNotFoundException;

/**
 * Named demo scenarios for recruiter-friendly one-click simulations.
 */
public enum SimulationScenario {

    RUSH_HOUR(
            "rush-hour",
            "Rush hour traffic",
            "Publishes escalating congestion on urbanflow.traffic.updates"
    ),
    POLLUTION_SPIKE(
            "pollution-spike",
            "Pollution spike",
            "Publishes rising PM2.5 readings on urbanflow.sensor.raw"
    );

    private final String slug;
    private final String title;
    private final String description;

    SimulationScenario(String slug, String title, String description) {
        this.slug = slug;
        this.title = title;
        this.description = description;
    }

    public String slug() {
        return slug;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public static SimulationScenario fromSlug(String slug) {
        for (SimulationScenario scenario : values()) {
            if (scenario.slug.equalsIgnoreCase(slug)) {
                return scenario;
            }
        }
        throw new ResourceNotFoundException("Scenario", slug);
    }
}
