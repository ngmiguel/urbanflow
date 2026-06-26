package com.urbanflow.analytics.presentation.dto;

import com.urbanflow.analytics.domain.model.ZoneKpi;

public record ZoneKpiResponse(
        String zoneId,
        String congestionLevel,
        double averageSpeedKmh,
        long alertCount,
        long incidentCount,
        long anomalyCount,
        long trafficUpdateCount,
        Double airQualityAverage,
        String updatedAt
) {

    public static ZoneKpiResponse from(ZoneKpi kpi) {
        return new ZoneKpiResponse(
                kpi.getZoneId(),
                kpi.getCongestionLevel().name(),
                kpi.getAverageSpeedKmh(),
                kpi.getAlertCount(),
                kpi.getIncidentCount(),
                kpi.getAnomalyCount(),
                kpi.getTrafficUpdateCount(),
                kpi.getAirQualityAverage(),
                kpi.getUpdatedAt() != null ? kpi.getUpdatedAt().toString() : null
        );
    }
}
