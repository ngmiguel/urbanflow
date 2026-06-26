package com.urbanflow.alert.infrastructure.persistence.entity;

import com.urbanflow.alert.domain.model.AlertSeverity;
import com.urbanflow.alert.domain.model.AlertStatus;
import com.urbanflow.common.enums.CongestionLevel;
import com.urbanflow.alert.domain.model.RuleSourceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "alert_rules")
public class AlertRuleEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false, length = 50)
    private RuleSourceType sourceType;

    @Column(name = "zone_id", length = 100)
    private String zoneId;

    @Enumerated(EnumType.STRING)
    @Column(name = "output_severity", nullable = false, length = 50)
    private AlertSeverity outputSeverity;

    @Column(nullable = false)
    private boolean enabled;

    @Enumerated(EnumType.STRING)
    @Column(name = "incident_min_severity", length = 50)
    private AlertSeverity incidentMinSeverity;

    @Enumerated(EnumType.STRING)
    @Column(name = "min_congestion_level", length = 50)
    private CongestionLevel minCongestionLevel;

    @Column(name = "sensor_type", length = 100)
    private String sensorType;

    @Column(name = "sensor_threshold")
    private Double sensorThreshold;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

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
