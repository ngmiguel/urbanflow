package com.urbanflow.alert.infrastructure.messaging;

import com.urbanflow.alert.application.port.AnomalyEventPublisher;
import com.urbanflow.common.constant.KafkaTopics;
import com.urbanflow.events.alert.AnomalyDetectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class KafkaAnomalyEventPublisher implements AnomalyEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaAnomalyEventPublisher.class);

    private final KafkaTemplate<String, AnomalyDetectedEvent> kafkaTemplate;

    public KafkaAnomalyEventPublisher(KafkaTemplate<String, AnomalyDetectedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(AnomalyDetectedEvent event) {
        kafkaTemplate.send(KafkaTopics.ANOMALY_DETECTED, event.zoneId(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish anomaly event for zone {}", event.zoneId(), ex);
                    } else {
                        log.info("Published anomaly event to {}", KafkaTopics.ANOMALY_DETECTED);
                    }
                });
    }
}
