package com.urbanflow.analytics.infrastructure.persistence.entity;

import com.urbanflow.common.enums.CongestionLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "zone_kpis")
public class ZoneKpiEntity {

    @Id
    @Column(name = "zone_id", length = 100)
    private String zoneId;

    @Enumerated(EnumType.STRING)
    @Column(name = "congestion_level", nullable = false, length = 50)
    private CongestionLevel congestionLevel;

    @Column(name = "average_speed_kmh", nullable = false)
    private double averageSpeedKmh;

    @Column(name = "alert_count", nullable = false)
    private long alertCount;

    @Column(name = "incident_count", nullable = false)
    private long incidentCount;

    @Column(name = "anomaly_count", nullable = false)
    private long anomalyCount;

    @Column(name = "traffic_update_count", nullable = false)
    private long trafficUpdateCount;

    @Column(name = "air_quality_sum", nullable = false)
    private double airQualitySum;

    @Column(name = "air_quality_samples", nullable = false)
    private long airQualitySamples;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public CongestionLevel getCongestionLevel() {
        return congestionLevel;
    }

    public void setCongestionLevel(CongestionLevel congestionLevel) {
        this.congestionLevel = congestionLevel;
    }

    public double getAverageSpeedKmh() {
        return averageSpeedKmh;
    }

    public void setAverageSpeedKmh(double averageSpeedKmh) {
        this.averageSpeedKmh = averageSpeedKmh;
    }

    public long getAlertCount() {
        return alertCount;
    }

    public void setAlertCount(long alertCount) {
        this.alertCount = alertCount;
    }

    public long getIncidentCount() {
        return incidentCount;
    }

    public void setIncidentCount(long incidentCount) {
        this.incidentCount = incidentCount;
    }

    public long getAnomalyCount() {
        return anomalyCount;
    }

    public void setAnomalyCount(long anomalyCount) {
        this.anomalyCount = anomalyCount;
    }

    public long getTrafficUpdateCount() {
        return trafficUpdateCount;
    }

    public void setTrafficUpdateCount(long trafficUpdateCount) {
        this.trafficUpdateCount = trafficUpdateCount;
    }

    public double getAirQualitySum() {
        return airQualitySum;
    }

    public void setAirQualitySum(double airQualitySum) {
        this.airQualitySum = airQualitySum;
    }

    public long getAirQualitySamples() {
        return airQualitySamples;
    }

    public void setAirQualitySamples(long airQualitySamples) {
        this.airQualitySamples = airQualitySamples;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
