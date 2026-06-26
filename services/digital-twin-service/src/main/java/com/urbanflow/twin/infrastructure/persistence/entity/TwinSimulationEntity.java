package com.urbanflow.twin.infrastructure.persistence.entity;

import com.urbanflow.common.enums.CongestionLevel;
import com.urbanflow.twin.domain.model.SimulationStatus;
import com.urbanflow.twin.domain.model.WhatIfScenarioType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "twin_simulations")
public class TwinSimulationEntity {

    @Id
    private UUID id;

    @Column(name = "zone_id", nullable = false, length = 100)
    private String zoneId;

    @Enumerated(EnumType.STRING)
    @Column(name = "scenario_type", nullable = false, length = 50)
    private WhatIfScenarioType scenarioType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SimulationStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "baseline_congestion", nullable = false, length = 50)
    private CongestionLevel baselineCongestion;

    @Column(name = "baseline_speed_kmh", nullable = false)
    private double baselineSpeedKmh;

    @Enumerated(EnumType.STRING)
    @Column(name = "projected_congestion", nullable = false, length = 50)
    private CongestionLevel projectedCongestion;

    @Column(name = "projected_speed_kmh", nullable = false)
    private double projectedSpeedKmh;

    @Column(name = "estimated_delay_minutes", nullable = false)
    private int estimatedDelayMinutes;

    @Column(name = "impact_summary", nullable = false, length = 500)
    private String impactSummary;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

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
