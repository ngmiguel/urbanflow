package com.urbanflow.twin.domain.model;

import com.urbanflow.common.enums.CongestionLevel;

import java.time.Instant;
import java.util.UUID;

public class TwinSimulation {

    private UUID id;
    private String zoneId;
    private WhatIfScenarioType scenarioType;
    private SimulationStatus status;
    private CongestionLevel baselineCongestion;
    private double baselineSpeedKmh;
    private CongestionLevel projectedCongestion;
    private double projectedSpeedKmh;
    private int estimatedDelayMinutes;
    private String impactSummary;
    private Instant createdAt;

    public TwinSimulation() {
    }

    public TwinSimulation(
            UUID id,
            String zoneId,
            WhatIfScenarioType scenarioType,
            SimulationStatus status,
            CongestionLevel baselineCongestion,
            double baselineSpeedKmh,
            CongestionLevel projectedCongestion,
            double projectedSpeedKmh,
            int estimatedDelayMinutes,
            String impactSummary,
            Instant createdAt
    ) {
        this.id = id;
        this.zoneId = zoneId;
        this.scenarioType = scenarioType;
        this.status = status;
        this.baselineCongestion = baselineCongestion;
        this.baselineSpeedKmh = baselineSpeedKmh;
        this.projectedCongestion = projectedCongestion;
        this.projectedSpeedKmh = projectedSpeedKmh;
        this.estimatedDelayMinutes = estimatedDelayMinutes;
        this.impactSummary = impactSummary;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public WhatIfScenarioType getScenarioType() {
        return scenarioType;
    }

    public void setScenarioType(WhatIfScenarioType scenarioType) {
        this.scenarioType = scenarioType;
    }

    public SimulationStatus getStatus() {
        return status;
    }

    public void setStatus(SimulationStatus status) {
        this.status = status;
    }

    public CongestionLevel getBaselineCongestion() {
        return baselineCongestion;
    }

    public void setBaselineCongestion(CongestionLevel baselineCongestion) {
        this.baselineCongestion = baselineCongestion;
    }

    public double getBaselineSpeedKmh() {
        return baselineSpeedKmh;
    }

    public void setBaselineSpeedKmh(double baselineSpeedKmh) {
        this.baselineSpeedKmh = baselineSpeedKmh;
    }

    public CongestionLevel getProjectedCongestion() {
        return projectedCongestion;
    }

    public void setProjectedCongestion(CongestionLevel projectedCongestion) {
        this.projectedCongestion = projectedCongestion;
    }

    public double getProjectedSpeedKmh() {
        return projectedSpeedKmh;
    }

    public void setProjectedSpeedKmh(double projectedSpeedKmh) {
        this.projectedSpeedKmh = projectedSpeedKmh;
    }

    public int getEstimatedDelayMinutes() {
        return estimatedDelayMinutes;
    }

    public void setEstimatedDelayMinutes(int estimatedDelayMinutes) {
        this.estimatedDelayMinutes = estimatedDelayMinutes;
    }

    public String getImpactSummary() {
        return impactSummary;
    }

    public void setImpactSummary(String impactSummary) {
        this.impactSummary = impactSummary;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
