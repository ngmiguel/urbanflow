package com.urbanflow.analytics.application.service;

import com.urbanflow.analytics.application.port.EventIdempotencyStore;
import com.urbanflow.analytics.domain.model.MetricType;
import com.urbanflow.analytics.domain.model.ZoneKpi;
import com.urbanflow.analytics.domain.repository.MetricHistoryRepository;
import com.urbanflow.analytics.domain.repository.ZoneKpiRepository;
import com.urbanflow.common.util.IdGenerator;
import com.urbanflow.events.alert.AlertTriggeredEvent;
import com.urbanflow.events.alert.AnomalyDetectedEvent;
import com.urbanflow.events.incident.IncidentEvent;
import com.urbanflow.events.sensor.SensorRawEvent;
import com.urbanflow.events.traffic.TrafficUpdateEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AnalyticsIngestionService {

    private static final String AIR_QUALITY_SENSOR = "AIR_QUALITY";

    private final ZoneKpiRepository zoneKpiRepository;
    private final MetricHistoryRepository metricHistoryRepository;
    private final EventIdempotencyStore idempotencyStore;

    public AnalyticsIngestionService(
            ZoneKpiRepository zoneKpiRepository,
            MetricHistoryRepository metricHistoryRepository,
            EventIdempotencyStore idempotencyStore
    ) {
        this.zoneKpiRepository = zoneKpiRepository;
        this.metricHistoryRepository = metricHistoryRepository;
        this.idempotencyStore = idempotencyStore;
    }

    @Transactional
    public void ingestTrafficEvent(TrafficUpdateEvent event) {
        if (shouldSkip(event.metadata().eventId())) {
            return;
        }

        ZoneKpi kpi = loadOrCreate(event.zoneId());
        kpi.setAverageSpeedKmh(event.averageSpeedKmh());
        kpi.setCongestionLevel(event.congestionLevel());
        kpi.setTrafficUpdateCount(kpi.getTrafficUpdateCount() + 1);
        kpi.setUpdatedAt(event.metadata().occurredAt());
        zoneKpiRepository.save(kpi);
        saveHistory(event.zoneId(), MetricType.TRAFFIC_SPEED, event.averageSpeedKmh(), event.metadata().occurredAt());
        idempotencyStore.markProcessed(event.metadata().eventId());
    }

    @Transactional
    public void ingestAlertEvent(AlertTriggeredEvent event) {
        if (shouldSkip(event.metadata().eventId())) {
            return;
        }

        ZoneKpi kpi = loadOrCreate(event.zoneId());
        kpi.setAlertCount(kpi.getAlertCount() + 1);
        kpi.setUpdatedAt(event.metadata().occurredAt());
        zoneKpiRepository.save(kpi);
        saveHistory(event.zoneId(), MetricType.ALERT, 1.0, event.metadata().occurredAt());
        idempotencyStore.markProcessed(event.metadata().eventId());
    }

    @Transactional
    public void ingestAnomalyEvent(AnomalyDetectedEvent event) {
        if (shouldSkip(event.metadata().eventId())) {
            return;
        }

        ZoneKpi kpi = loadOrCreate(event.zoneId());
        kpi.setAnomalyCount(kpi.getAnomalyCount() + 1);
        kpi.setUpdatedAt(event.metadata().occurredAt());
        zoneKpiRepository.save(kpi);
        saveHistory(event.zoneId(), MetricType.ANOMALY, event.score(), event.metadata().occurredAt());
        idempotencyStore.markProcessed(event.metadata().eventId());
    }

    @Transactional
    public void ingestIncidentEvent(IncidentEvent event) {
        if (shouldSkip(event.metadata().eventId())) {
            return;
        }
        if (event.eventType() != IncidentEvent.IncidentEventType.REPORTED) {
            idempotencyStore.markProcessed(event.metadata().eventId());
            return;
        }

        ZoneKpi kpi = loadOrCreate(event.zoneId());
        kpi.setIncidentCount(kpi.getIncidentCount() + 1);
        kpi.setUpdatedAt(event.metadata().occurredAt());
        zoneKpiRepository.save(kpi);
        saveHistory(event.zoneId(), MetricType.INCIDENT, 1.0, event.metadata().occurredAt());
        idempotencyStore.markProcessed(event.metadata().eventId());
    }

    @Transactional
    public void ingestSensorEvent(SensorRawEvent event) {
        if (shouldSkip(event.metadata().eventId())) {
            return;
        }
        if (!AIR_QUALITY_SENSOR.equalsIgnoreCase(event.sensorType())) {
            idempotencyStore.markProcessed(event.metadata().eventId());
            return;
        }

        ZoneKpi kpi = loadOrCreate(event.zoneId());
        kpi.setAirQualitySum(kpi.getAirQualitySum() + event.value());
        kpi.setAirQualitySamples(kpi.getAirQualitySamples() + 1);
        kpi.setUpdatedAt(event.metadata().occurredAt());
        zoneKpiRepository.save(kpi);
        saveHistory(event.zoneId(), MetricType.AIR_QUALITY, event.value(), event.metadata().occurredAt());
        idempotencyStore.markProcessed(event.metadata().eventId());
    }

    private ZoneKpi loadOrCreate(String zoneId) {
        return zoneKpiRepository.findByZoneId(zoneId).orElseGet(() -> new ZoneKpi(zoneId));
    }

    private void saveHistory(String zoneId, MetricType metricType, double value, Instant recordedAt) {
        metricHistoryRepository.save(new com.urbanflow.analytics.domain.model.MetricHistoryEntry(
                IdGenerator.newId(),
                zoneId,
                metricType,
                value,
                recordedAt
        ));
    }

    private boolean shouldSkip(UUID eventId) {
        return idempotencyStore.alreadyProcessed(eventId);
    }
}
