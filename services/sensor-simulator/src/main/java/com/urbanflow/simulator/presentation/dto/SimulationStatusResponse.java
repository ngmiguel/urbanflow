package com.urbanflow.simulator.presentation.dto;

import com.urbanflow.simulator.domain.model.SimulationState;

public record SimulationStatusResponse(
        String status,
        String scenario,
        String zoneId,
        String correlationId,
        int eventsPublished,
        String startedAt,
        String endsAt
) {

    public static SimulationStatusResponse from(SimulationState state) {
        return new SimulationStatusResponse(
                state.status().name(),
                state.scenario() != null ? state.scenario().slug() : null,
                state.zoneId(),
                state.correlationId(),
                state.eventsPublished(),
                state.startedAt() != null ? state.startedAt().toString() : null,
                state.endsAt() != null ? state.endsAt().toString() : null
        );
    }
}
