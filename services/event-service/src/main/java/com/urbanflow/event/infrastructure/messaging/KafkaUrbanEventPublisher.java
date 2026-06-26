package com.urbanflow.event.infrastructure.messaging;

import com.urbanflow.common.constant.KafkaTopics;
import com.urbanflow.event.application.port.UrbanEventPublisher;
import com.urbanflow.events.urban.UrbanPlannedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class KafkaUrbanEventPublisher implements UrbanEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaUrbanEventPublisher.class);

    private final KafkaTemplate<String, UrbanPlannedEvent> kafkaTemplate;

    public KafkaUrbanEventPublisher(KafkaTemplate<String, UrbanPlannedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(UrbanPlannedEvent event) {
        kafkaTemplate.send(KafkaTopics.URBAN_EVENTS, event.eventId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish urban event {}", event.eventId(), ex);
                    } else {
                        log.info("Published urban event {} ({}) to {}", event.eventId(), event.lifecycleType(), KafkaTopics.URBAN_EVENTS);
                    }
                });
    }
}
