package com.urbanflow.incident.infrastructure.messaging;

import com.urbanflow.common.constant.KafkaTopics;
import com.urbanflow.events.incident.IncidentEvent;
import com.urbanflow.incident.application.port.IncidentEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class KafkaIncidentEventPublisher implements IncidentEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaIncidentEventPublisher.class);

    private final KafkaTemplate<String, IncidentEvent> kafkaTemplate;

    public KafkaIncidentEventPublisher(KafkaTemplate<String, IncidentEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(IncidentEvent event) {
        kafkaTemplate.send(KafkaTopics.INCIDENT_EVENTS, event.incidentId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish incident event for {}", event.incidentId(), ex);
                    } else {
                        log.info("Published incident event {} to {}", event.incidentId(), KafkaTopics.INCIDENT_EVENTS);
                    }
                });
    }
}
