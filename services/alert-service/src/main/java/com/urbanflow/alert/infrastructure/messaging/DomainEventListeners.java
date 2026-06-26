package com.urbanflow.alert.infrastructure.messaging;

import com.urbanflow.alert.application.service.AlertCorrelationService;
import com.urbanflow.common.constant.KafkaTopics;
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

    private final AlertCorrelationService alertCorrelationService;

    public DomainEventListeners(AlertCorrelationService alertCorrelationService) {
        this.alertCorrelationService = alertCorrelationService;
    }

    @KafkaListener(
            topics = KafkaTopics.INCIDENT_EVENTS,
            groupId = "alert-service-incidents",
            containerFactory = "incidentKafkaListenerContainerFactory"
    )
    public void onIncidentEvent(IncidentEvent event) {
        log.debug("Received incident event {}", event.incidentId());
        alertCorrelationService.processIncidentEvent(event);
    }

    @KafkaListener(
            topics = KafkaTopics.TRAFFIC_UPDATES,
            groupId = "alert-service-traffic",
            containerFactory = "trafficKafkaListenerContainerFactory"
    )
    public void onTrafficEvent(TrafficUpdateEvent event) {
        log.debug("Received traffic event for segment {}", event.segmentId());
        alertCorrelationService.processTrafficEvent(event);
    }

    @KafkaListener(
            topics = KafkaTopics.SENSOR_RAW,
            groupId = "alert-service-sensors",
            containerFactory = "sensorKafkaListenerContainerFactory"
    )
    public void onSensorEvent(SensorRawEvent event) {
        log.debug("Received sensor event from device {}", event.deviceId());
        alertCorrelationService.processSensorEvent(event);
    }
}
