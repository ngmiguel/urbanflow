package com.urbanflow.alert.domain.model;

import com.urbanflow.common.enums.CongestionLevel;

import java.time.Instant;
import java.util.UUID;

public class AlertRule {

    private UUID id;
    private String name;
    private RuleSourceType sourceType;
    private String zoneId;
    private AlertSeverity outputSeverity;
    private boolean enabled;
    private AlertSeverity incidentMinSeverity;
    private CongestionLevel minCongestionLevel;
    private String sensorType;
    private Double sensorThreshold;
    private Instant createdAt;
    private Instant updatedAt;

    public AlertRule() {
    }

    public AlertRule(
            UUID id,
            String name,
            RuleSourceType sourceType,
            String zoneId,
            AlertSeverity outputSeverity,
            boolean enabled,
            AlertSeverity incidentMinSeverity,
            CongestionLevel minCongestionLevel,
            String sensorType,
            Double sensorThreshold,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.sourceType = sourceType;
        this.zoneId = zoneId;
        this.outputSeverity = outputSeverity;
        this.enabled = enabled;
        this.incidentMinSeverity = incidentMinSeverity;
        this.minCongestionLevel = minCongestionLevel;
        this.sensorType = sensorType;
        this.sensorThreshold = sensorThreshold;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static AlertRule create(
            String name,
            RuleSourceType sourceType,
            String zoneId,
            AlertSeverity outputSeverity,
            AlertSeverity incidentMinSeverity,
            CongestionLevel minCongestionLevel,
            String sensorType,
            Double sensorThreshold
    ) {
        Instant now = Instant.now();
        AlertRule rule = new AlertRule();
        rule.id = UUID.randomUUID();
        rule.name = name;
        rule.sourceType = sourceType;
        rule.zoneId = zoneId;
        rule.outputSeverity = outputSeverity;
        rule.enabled = true;
        rule.incidentMinSeverity = incidentMinSeverity;
        rule.minCongestionLevel = minCongestionLevel;
        rule.sensorType = sensorType;
        rule.sensorThreshold = sensorThreshold;
        rule.createdAt = now;
        rule.updatedAt = now;
        return rule;
    }

    public void update(
            String name,
            String zoneId,
            AlertSeverity outputSeverity,
            boolean enabled,
            AlertSeverity incidentMinSeverity,
            CongestionLevel minCongestionLevel,
            String sensorType,
            Double sensorThreshold
    ) {
        this.name = name;
        this.zoneId = zoneId;
        this.outputSeverity = outputSeverity;
        this.enabled = enabled;
        this.incidentMinSeverity = incidentMinSeverity;
        this.minCongestionLevel = minCongestionLevel;
        this.sensorType = sensorType;
        this.sensorThreshold = sensorThreshold;
        this.updatedAt = Instant.now();
    }

    public boolean matchesZone(String eventZoneId) {
        return zoneId == null || zoneId.isBlank() || zoneId.equals(eventZoneId);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RuleSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(RuleSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public AlertSeverity getOutputSeverity() {
        return outputSeverity;
    }

    public void setOutputSeverity(AlertSeverity outputSeverity) {
        this.outputSeverity = outputSeverity;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public AlertSeverity getIncidentMinSeverity() {
        return incidentMinSeverity;
    }

    public void setIncidentMinSeverity(AlertSeverity incidentMinSeverity) {
        this.incidentMinSeverity = incidentMinSeverity;
    }

    public CongestionLevel getMinCongestionLevel() {
        return minCongestionLevel;
    }

    public void setMinCongestionLevel(CongestionLevel minCongestionLevel) {
        this.minCongestionLevel = minCongestionLevel;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public Double getSensorThreshold() {
        return sensorThreshold;
    }

    public void setSensorThreshold(Double sensorThreshold) {
        this.sensorThreshold = sensorThreshold;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
