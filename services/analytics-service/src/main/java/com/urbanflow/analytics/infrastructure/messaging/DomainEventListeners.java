package com.urbanflow.analytics.infrastructure.messaging;

import com.urbanflow.analytics.application.service.AnalyticsIngestionService;
import com.urbanflow.common.constant.KafkaTopics;
import com.urbanflow.events.alert.AlertTriggeredEvent;
import com.urbanflow.events.alert.AnomalyDetectedEvent;
import com.urbanflow.events.incident.IncidentEvent;
import com.urbanflow.events.sensor.SensorRawEvent;
import com.urbanflow.events.traffic.TrafficUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class DomainEventListeners {

    private static final Logger log = LoggerFactory.getLogger(DomainEventListeners.class);

    private final AnalyticsIngestionService analyticsIngestionService;

    public DomainEventListeners(AnalyticsIngestionService analyticsIngestionService) {
        this.analyticsIngestionService = analyticsIngestionService;
    }

    @KafkaListener(
            topics = KafkaTopics.TRAFFIC_UPDATES,
            groupId = "analytics-service",
            containerFactory = "trafficKafkaListenerContainerFactory"
    )
    public void onTrafficUpdate(TrafficUpdateEvent event) {
        log.debug("Aggregating traffic metrics for zone {}", event.zoneId());
        analyticsIngestionService.ingestTrafficEvent(event);
    }

    @KafkaListener(
            topics = KafkaTopics.ALERT_EVENTS,
            groupId = "analytics-service",
            containerFactory = "alertKafkaListenerContainerFactory"
    )
    public void onAlertTriggered(AlertTriggeredEvent event) {
        log.debug("Aggregating alert metrics for zone {}", event.zoneId());
        analyticsIngestionService.ingestAlertEvent(event);
    }

    @KafkaListener(
            topics = KafkaTopics.ANOMALY_DETECTED,
            groupId = "analytics-service",
            containerFactory = "anomalyKafkaListenerContainerFactory"
    )
    public void onAnomalyDetected(AnomalyDetectedEvent event) {
        log.debug("Aggregating anomaly metrics for zone {}", event.zoneId());
        analyticsIngestionService.ingestAnomalyEvent(event);
    }

    @KafkaListener(
            topics = KafkaTopics.INCIDENT_EVENTS,
            groupId = "analytics-service",
            containerFactory = "incidentKafkaListenerContainerFactory"
    )
    public void onIncidentEvent(IncidentEvent event) {
        log.debug("Aggregating incident metrics for zone {}", event.zoneId());
        analyticsIngestionService.ingestIncidentEvent(event);
    }

    @KafkaListener(
            topics = KafkaTopics.SENSOR_RAW,
            groupId = "analytics-service-sensors",
            containerFactory = "sensorKafkaListenerContainerFactory"
    )
    public void onSensorRawEvent(SensorRawEvent event) {
        log.debug("Aggregating sensor metrics for zone {}", event.zoneId());
        analyticsIngestionService.ingestSensorEvent(event);
    }
}
