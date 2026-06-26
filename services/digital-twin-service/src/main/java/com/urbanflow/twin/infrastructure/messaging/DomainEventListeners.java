package com.urbanflow.twin.infrastructure.messaging;

import com.urbanflow.common.constant.KafkaTopics;
import com.urbanflow.events.incident.IncidentEvent;
import com.urbanflow.events.traffic.TrafficUpdateEvent;
import com.urbanflow.twin.application.service.DigitalTwinApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class DomainEventListeners {

    private static final Logger log = LoggerFactory.getLogger(DomainEventListeners.class);

    private final DigitalTwinApplicationService digitalTwinApplicationService;

    public DomainEventListeners(DigitalTwinApplicationService digitalTwinApplicationService) {
        this.digitalTwinApplicationService = digitalTwinApplicationService;
    }

    @KafkaListener(
            topics = KafkaTopics.TRAFFIC_UPDATES,
            groupId = "digital-twin-service",
            containerFactory = "trafficKafkaListenerContainerFactory"
    )
    public void onTrafficUpdate(TrafficUpdateEvent event) {
        log.debug("Updating twin baseline from traffic event in zone {}", event.zoneId());
        digitalTwinApplicationService.ingestTrafficEvent(event);
    }

    @KafkaListener(
            topics = KafkaTopics.INCIDENT_EVENTS,
            groupId = "digital-twin-service",
            containerFactory = "incidentKafkaListenerContainerFactory"
    )
    public void onIncidentEvent(IncidentEvent event) {
        log.debug("Updating twin baseline from incident event in zone {}", event.zoneId());
        digitalTwinApplicationService.ingestIncidentEvent(event);
    }
}
