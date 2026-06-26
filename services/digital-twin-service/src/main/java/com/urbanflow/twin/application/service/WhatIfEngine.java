package com.urbanflow.twin.application.service;

import com.urbanflow.common.enums.CongestionLevel;
import com.urbanflow.common.util.IdGenerator;
import com.urbanflow.twin.domain.model.SimulationStatus;
import com.urbanflow.twin.domain.model.TwinSimulation;
import com.urbanflow.twin.domain.model.TwinZoneState;
import com.urbanflow.twin.domain.model.WhatIfScenarioType;
import org.springframework.stereotype.Component;

@Component
public class WhatIfEngine {

    public TwinSimulation simulate(
            TwinZoneState baseline,
            WhatIfScenarioType scenarioType,
            String incidentSeverity,
            Integer closureDurationMinutes
    ) {
        CongestionLevel projectedCongestion = baseline.congestionLevel();
        double projectedSpeed = baseline.averageSpeedKmh();
        int delayMinutes = 0;
        String summary;

        switch (scenarioType) {
            case INCIDENT_IMPACT -> {
                int severityBoost = severityBoost(incidentSeverity);
                projectedCongestion = escalate(baseline.congestionLevel(), severityBoost);
                projectedSpeed = Math.max(5.0, baseline.averageSpeedKmh() * speedFactor(severityBoost));
                delayMinutes = 10 + (severityBoost * 8) + baseline.activeIncidents() * 3;
                summary = "Incident (%s) would raise congestion from %s to %s"
                        .formatted(
                                incidentSeverity != null ? incidentSeverity : "MEDIUM",
                                baseline.congestionLevel(),
                                projectedCongestion
                        );
            }
            case ROAD_CLOSURE -> {
                int duration = closureDurationMinutes != null ? closureDurationMinutes : 60;
                projectedCongestion = CongestionLevel.GRIDLOCK;
                projectedSpeed = Math.max(5.0, baseline.averageSpeedKmh() * 0.35);
                delayMinutes = Math.min(120, duration / 2 + 15);
                summary = "Road closure (%d min) would push zone to %s"
                        .formatted(duration, projectedCongestion);
            }
            default -> summary = "No impact computed";
        }

        TwinSimulation simulation = new TwinSimulation();
        simulation.setId(IdGenerator.newId());
        simulation.setZoneId(baseline.zoneId());
        simulation.setScenarioType(scenarioType);
        simulation.setStatus(SimulationStatus.COMPLETED);
        simulation.setBaselineCongestion(baseline.congestionLevel());
        simulation.setBaselineSpeedKmh(baseline.averageSpeedKmh());
        simulation.setProjectedCongestion(projectedCongestion);
        simulation.setProjectedSpeedKmh(projectedSpeed);
        simulation.setEstimatedDelayMinutes(delayMinutes);
        simulation.setImpactSummary(summary);
        simulation.setCreatedAt(java.time.Instant.now());
        return simulation;
    }

    public TwinZoneState toProjectedState(TwinSimulation simulation) {
        return new TwinZoneState(
                simulation.getZoneId(),
                simulation.getProjectedCongestion(),
                simulation.getProjectedSpeedKmh(),
                (int) Math.round(simulation.getBaselineSpeedKmh() * 4),
                simulation.getBaselineCongestion() == CongestionLevel.GRIDLOCK ? 2 : 1,
                java.time.Instant.now()
        );
    }

    private int severityBoost(String severity) {
        if (severity == null) {
            return 1;
        }
        return switch (severity.toUpperCase()) {
            case "LOW" -> 1;
            case "HIGH", "CRITICAL" -> 2;
            default -> 1;
        };
    }

    private CongestionLevel escalate(CongestionLevel current, int steps) {
        CongestionLevel[] levels = CongestionLevel.values();
        int index = Math.min(levels.length - 1, current.ordinal() + steps);
        return levels[index];
    }

    private double speedFactor(int severityBoost) {
        return switch (severityBoost) {
            case 2 -> 0.45;
            default -> 0.65;
        };
    }
}
