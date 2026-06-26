package com.urbanflow.twin.infrastructure.persistence.mapper;

import com.urbanflow.twin.domain.model.TwinSimulation;
import com.urbanflow.twin.infrastructure.persistence.entity.TwinSimulationEntity;

public final class TwinSimulationMapper {

    private TwinSimulationMapper() {
    }

    public static TwinSimulationEntity toEntity(TwinSimulation simulation) {
        TwinSimulationEntity entity = new TwinSimulationEntity();
        entity.setId(simulation.getId());
        entity.setZoneId(simulation.getZoneId());
        entity.setScenarioType(simulation.getScenarioType());
        entity.setStatus(simulation.getStatus());
        entity.setBaselineCongestion(simulation.getBaselineCongestion());
        entity.setBaselineSpeedKmh(simulation.getBaselineSpeedKmh());
        entity.setProjectedCongestion(simulation.getProjectedCongestion());
        entity.setProjectedSpeedKmh(simulation.getProjectedSpeedKmh());
        entity.setEstimatedDelayMinutes(simulation.getEstimatedDelayMinutes());
        entity.setImpactSummary(simulation.getImpactSummary());
        entity.setCreatedAt(simulation.getCreatedAt());
        return entity;
    }

    public static TwinSimulation toDomain(TwinSimulationEntity entity) {
        return new TwinSimulation(
                entity.getId(),
                entity.getZoneId(),
                entity.getScenarioType(),
                entity.getStatus(),
                entity.getBaselineCongestion(),
                entity.getBaselineSpeedKmh(),
                entity.getProjectedCongestion(),
                entity.getProjectedSpeedKmh(),
                entity.getEstimatedDelayMinutes(),
                entity.getImpactSummary(),
                entity.getCreatedAt()
        );
    }
}
