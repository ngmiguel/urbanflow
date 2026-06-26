package com.urbanflow.analytics.infrastructure.persistence.mapper;

import com.urbanflow.analytics.domain.model.MetricHistoryEntry;
import com.urbanflow.analytics.domain.model.ZoneKpi;
import com.urbanflow.analytics.infrastructure.persistence.entity.MetricHistoryEntity;
import com.urbanflow.analytics.infrastructure.persistence.entity.ZoneKpiEntity;

public final class AnalyticsMapper {

    private AnalyticsMapper() {
    }

    public static ZoneKpiEntity toEntity(ZoneKpi kpi) {
        ZoneKpiEntity entity = new ZoneKpiEntity();
        entity.setZoneId(kpi.getZoneId());
        entity.setCongestionLevel(kpi.getCongestionLevel());
        entity.setAverageSpeedKmh(kpi.getAverageSpeedKmh());
        entity.setAlertCount(kpi.getAlertCount());
        entity.setIncidentCount(kpi.getIncidentCount());
        entity.setAnomalyCount(kpi.getAnomalyCount());
        entity.setTrafficUpdateCount(kpi.getTrafficUpdateCount());
        entity.setAirQualitySum(kpi.getAirQualitySum());
        entity.setAirQualitySamples(kpi.getAirQualitySamples());
        entity.setUpdatedAt(kpi.getUpdatedAt());
        return entity;
    }

    public static ZoneKpi toDomain(ZoneKpiEntity entity) {
        ZoneKpi kpi = new ZoneKpi();
        kpi.setZoneId(entity.getZoneId());
        kpi.setCongestionLevel(entity.getCongestionLevel());
        kpi.setAverageSpeedKmh(entity.getAverageSpeedKmh());
        kpi.setAlertCount(entity.getAlertCount());
        kpi.setIncidentCount(entity.getIncidentCount());
        kpi.setAnomalyCount(entity.getAnomalyCount());
        kpi.setTrafficUpdateCount(entity.getTrafficUpdateCount());
        kpi.setAirQualitySum(entity.getAirQualitySum());
        kpi.setAirQualitySamples(entity.getAirQualitySamples());
        kpi.setUpdatedAt(entity.getUpdatedAt());
        return kpi;
    }

    public static MetricHistoryEntity toEntity(MetricHistoryEntry entry) {
        MetricHistoryEntity entity = new MetricHistoryEntity();
        entity.setId(entry.id());
        entity.setZoneId(entry.zoneId());
        entity.setMetricType(entry.metricType());
        entity.setMetricValue(entry.value());
        entity.setRecordedAt(entry.recordedAt());
        return entity;
    }

    public static MetricHistoryEntry toDomain(MetricHistoryEntity entity) {
        return new MetricHistoryEntry(
                entity.getId(),
                entity.getZoneId(),
                entity.getMetricType(),
                entity.getMetricValue(),
                entity.getRecordedAt()
        );
    }
}
