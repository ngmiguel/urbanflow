package com.urbanflow.simulator.domain.model;

import java.time.Instant;

public record SimulationState(
        SimulationStatus status,
        SimulationScenario scenario,
        String zoneId,
        String correlationId,
        int eventsPublished,
        Instant startedAt,
        Instant endsAt
) {

    public static SimulationState idle() {
        return new SimulationState(SimulationStatus.IDLE, null, null, null, 0, null, null);
    }
}
