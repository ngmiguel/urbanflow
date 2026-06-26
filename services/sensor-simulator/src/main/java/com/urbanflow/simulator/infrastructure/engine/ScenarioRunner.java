package com.urbanflow.simulator.infrastructure.engine;

import com.urbanflow.simulator.domain.model.SimulationScenario;

public interface ScenarioRunner {

    SimulationScenario scenario();

    int tick(SimulationTickContext context);
}
