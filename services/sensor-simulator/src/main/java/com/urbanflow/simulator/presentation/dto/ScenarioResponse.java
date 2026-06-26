package com.urbanflow.simulator.presentation.dto;

import com.urbanflow.simulator.domain.model.SimulationScenario;

public record ScenarioResponse(
        String slug,
        String title,
        String description
) {

    public static ScenarioResponse from(SimulationScenario scenario) {
        return new ScenarioResponse(scenario.slug(), scenario.title(), scenario.description());
    }
}
