package com.urbanflow.twin.presentation.dto;

import com.urbanflow.twin.domain.model.TwinSimulation;

public record SimulationResponse(
        String id,
        String zoneId,
        String scenarioType,
        String status,
        String baselineCongestion,
        double baselineSpeedKmh,
        String projectedCongestion,
        double projectedSpeedKmh,
        int estimatedDelayMinutes,
        String impactSummary,
        String createdAt
) {

    public static SimulationResponse from(TwinSimulation simulation) {
        return new SimulationResponse(
                simulation.getId().toString(),
                simulation.getZoneId(),
                simulation.getScenarioType().name(),
                simulation.getStatus().name(),
                simulation.getBaselineCongestion().name(),
                simulation.getBaselineSpeedKmh(),
                simulation.getProjectedCongestion().name(),
                simulation.getProjectedSpeedKmh(),
                simulation.getEstimatedDelayMinutes(),
                simulation.getImpactSummary(),
                simulation.getCreatedAt().toString()
        );
    }
}
