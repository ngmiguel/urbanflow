package com.urbanflow.alert.application.service;

import com.urbanflow.alert.application.port.AlertEventPublisher;
import com.urbanflow.alert.application.port.AnomalyEventPublisher;
import com.urbanflow.alert.application.port.EventIdempotencyStore;
import com.urbanflow.alert.domain.model.Alert;
import com.urbanflow.alert.domain.model.AlertRule;
import com.urbanflow.alert.domain.model.AlertSeverity;
import com.urbanflow.alert.domain.model.RuleSourceType;
import com.urbanflow.alert.domain.repository.AlertRepository;
import com.urbanflow.alert.domain.repository.AlertRuleRepository;
import com.urbanflow.common.enums.CongestionLevel;
import com.urbanflow.common.util.IdGenerator;
import com.urbanflow.events.EventMetadata;
import com.urbanflow.events.alert.AlertTriggeredEvent;
import com.urbanflow.events.alert.AnomalyDetectedEvent;
import com.urbanflow.events.incident.IncidentEvent;
import com.urbanflow.events.sensor.SensorRawEvent;
import com.urbanflow.events.traffic.TrafficUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AlertCorrelationService {

    private static final Logger log = LoggerFactory.getLogger(AlertCorrelationService.class);
    private static final double ANOMALY_SCORE_MULTIPLIER = 1.5;

    private final AlertRuleRepository alertRuleRepository;
    private final AlertRepository alertRepository;
    private final AlertEventPublisher alertEventPublisher;
    private final AnomalyEventPublisher anomalyEventPublisher;
    private final EventIdempotencyStore idempotencyStore;

    public AlertCorrelationService(
            AlertRuleRepository alertRuleRepository,
            AlertRepository alertRepository,
            AlertEventPublisher alertEventPublisher,
            AnomalyEventPublisher anomalyEventPublisher,
            EventIdempotencyStore idempotencyStore
    ) {
        this.alertRuleRepository = alertRuleRepository;
        this.alertRepository = alertRepository;
        this.alertEventPublisher = alertEventPublisher;
        this.anomalyEventPublisher = anomalyEventPublisher;
        this.idempotencyStore = idempotencyStore;
    }

    @Transactional
    public void processIncidentEvent(IncidentEvent event) {
        if (shouldSkip(event.metadata().eventId())) {
            return;
        }
        if (event.eventType() == IncidentEvent.IncidentEventType.RESOLVED) {
            idempotencyStore.markProcessed(event.metadata().eventId());
            return;
        }

        AlertSeverity eventSeverity = parseSeverity(event.severity());
        alertRuleRepository.findBySourceTypeAndEnabledTrue(RuleSourceType.INCIDENT).stream()
                .filter(rule -> rule.matchesZone(event.zoneId()))
                .filter(rule -> rule.getIncidentMinSeverity() != null)
                .filter(rule -> severityRank(eventSeverity) >= severityRank(rule.getIncidentMinSeverity()))
                .forEach(rule -> triggerAlert(
                        rule,
                        "INCIDENT_" + event.eventType().name(),
                        "Incident alert: " + event.description(),
                        event.zoneId(),
                        event.metadata().eventId().toString(),
                        event.metadata().correlationId()
                ));

        idempotencyStore.markProcessed(event.metadata().eventId());
    }

    @Transactional
    public void processTrafficEvent(TrafficUpdateEvent event) {
        if (shouldSkip(event.metadata().eventId())) {
            return;
        }

        alertRuleRepository.findBySourceTypeAndEnabledTrue(RuleSourceType.TRAFFIC).stream()
                .filter(rule -> rule.matchesZone(event.zoneId()))
                .filter(rule -> rule.getMinCongestionLevel() != null)
                .filter(rule -> congestionRank(event.congestionLevel()) >= congestionRank(rule.getMinCongestionLevel()))
                .forEach(rule -> triggerAlert(
                        rule,
                        "TRAFFIC_" + event.eventType().name(),
                        "Traffic alert on %s: congestion %s".formatted(event.segmentName(), event.congestionLevel()),
                        event.zoneId(),
                        event.metadata().eventId().toString(),
                        event.metadata().correlationId()
                ));

        idempotencyStore.markProcessed(event.metadata().eventId());
    }

    @Transactional
    public void processSensorEvent(SensorRawEvent event) {
        if (shouldSkip(event.metadata().eventId())) {
            return;
        }

        alertRuleRepository.findBySourceTypeAndEnabledTrue(RuleSourceType.SENSOR).stream()
                .filter(rule -> rule.matchesZone(event.zoneId()))
                .filter(rule -> rule.getSensorThreshold() != null)
                .filter(rule -> rule.getSensorType() == null
                        || rule.getSensorType().equalsIgnoreCase(event.sensorType()))
                .filter(rule -> event.value() >= rule.getSensorThreshold())
                .forEach(rule -> {
                    triggerAlert(
                            rule,
                            "SENSOR_THRESHOLD",
                            "Sensor %s exceeded threshold (value=%.2f %s)".formatted(
                                    event.deviceId(), event.value(), event.unit()),
                            event.zoneId(),
                            event.metadata().eventId().toString(),
                            event.metadata().correlationId()
                    );

                    if (event.value() >= rule.getSensorThreshold() * ANOMALY_SCORE_MULTIPLIER) {
                        publishAnomaly(event, rule.getSensorThreshold());
                    }
                });

        idempotencyStore.markProcessed(event.metadata().eventId());
    }

    private boolean shouldSkip(UUID eventId) {
        if (idempotencyStore.alreadyProcessed(eventId)) {
            log.debug("Skipping already processed event {}", eventId);
            return true;
        }
        return false;
    }

    private void triggerAlert(
            AlertRule rule,
            String alertType,
            String message,
            String zoneId,
            String sourceEventId,
            String correlationId
    ) {
        Alert alert = Alert.trigger(
                rule.getId(),
                alertType,
                rule.getOutputSeverity(),
                message,
                zoneId,
                sourceEventId
        );
        Alert saved = alertRepository.save(alert);

        String corrId = correlationId != null && !correlationId.isBlank()
                ? correlationId
                : IdGenerator.newCorrelationId();

        alertEventPublisher.publish(new AlertTriggeredEvent(
                EventMetadata.create(alertType, "alert-service", corrId),
                saved.getId(),
                alertType,
                saved.getSeverity().name(),
                saved.getMessage(),
                saved.getZoneId(),
                sourceEventId
        ));

        log.info("Alert {} triggered by rule {} for zone {}", saved.getId(), rule.getId(), zoneId);
    }

    private void publishAnomaly(SensorRawEvent event, double threshold) {
        double score = event.value() / threshold;
        anomalyEventPublisher.publish(new AnomalyDetectedEvent(
                EventMetadata.create("SENSOR_ANOMALY", "alert-service", event.metadata().correlationId()),
                "SENSOR_SPIKE",
                score,
                event.zoneId(),
                event.deviceId(),
                "Sensor reading %.2f exceeds anomaly threshold".formatted(event.value())
        ));
    }

    private AlertSeverity parseSeverity(String severity) {
        try {
            return AlertSeverity.valueOf(severity);
        } catch (IllegalArgumentException ex) {
            return AlertSeverity.MEDIUM;
        }
    }

    private int severityRank(AlertSeverity severity) {
        return severity.ordinal();
    }

    private int congestionRank(CongestionLevel level) {
        return level.ordinal();
    }
}
