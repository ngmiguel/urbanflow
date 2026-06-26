package com.urbanflow.analytics.domain.model;

import com.urbanflow.common.enums.CongestionLevel;

import java.time.Instant;

public class ZoneKpi {

    private String zoneId;
    private CongestionLevel congestionLevel;
    private double averageSpeedKmh;
    private long alertCount;
    private long incidentCount;
    private long anomalyCount;
    private long trafficUpdateCount;
    private double airQualitySum;
    private long airQualitySamples;
    private Instant updatedAt;

    public ZoneKpi() {
    }

    public ZoneKpi(String zoneId) {
        this.zoneId = zoneId;
        this.congestionLevel = CongestionLevel.FREE_FLOW;
        this.averageSpeedKmh = 0.0;
        this.updatedAt = Instant.now();
    }

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

    public Double getAirQualityAverage() {
        if (airQualitySamples == 0) {
            return null;
        }
        return airQualitySum / airQualitySamples;
    }
}
