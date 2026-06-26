package com.urbanflow.simulator.infrastructure.engine;

import com.urbanflow.simulator.application.port.SimulationEventPublisher;
import com.urbanflow.simulator.domain.model.SimulationScenario;
import com.urbanflow.simulator.infrastructure.config.SimulatorProperties;

public record SimulationTickContext(
        String zoneId,
        String correlationId,
        int tickCount,
        SimulationEventPublisher publisher,
        SimulatorProperties properties
) {
}
