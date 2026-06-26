package com.urbanflow.twin.application.service;

import com.urbanflow.common.enums.CongestionLevel;
import com.urbanflow.twin.domain.model.TwinZoneState;
import com.urbanflow.twin.domain.model.WhatIfScenarioType;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WhatIfEngineTest {

    private final WhatIfEngine whatIfEngine = new WhatIfEngine();

    @Test
    void shouldEscalateCongestionForIncidentImpact() {
        TwinZoneState baseline = new TwinZoneState(
                "zone-casa-centre",
                CongestionLevel.MODERATE,
                35.0,
                300,
                1,
                Instant.now()
        );

        var simulation = whatIfEngine.simulate(
                baseline,
                WhatIfScenarioType.INCIDENT_IMPACT,
                "HIGH",
                null
        );

        assertEquals(CongestionLevel.GRIDLOCK, simulation.getProjectedCongestion());
        assertTrue(simulation.getProjectedSpeedKmh() < baseline.averageSpeedKmh());
        assertTrue(simulation.getEstimatedDelayMinutes() > 0);
    }

    @Test
    void shouldProjectGridlockForRoadClosure() {
        TwinZoneState baseline = TwinZoneState.baseline("zone-casa-centre");

        var simulation = whatIfEngine.simulate(
                baseline,
                WhatIfScenarioType.ROAD_CLOSURE,
                null,
                90
        );

        assertEquals(CongestionLevel.GRIDLOCK, simulation.getProjectedCongestion());
        assertTrue(simulation.getImpactSummary().contains("Road closure"));
    }
}
