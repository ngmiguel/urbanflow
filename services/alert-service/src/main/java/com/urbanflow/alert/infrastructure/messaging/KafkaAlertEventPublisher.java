package com.urbanflow.alert.infrastructure.messaging;

import com.urbanflow.alert.application.port.AlertEventPublisher;
import com.urbanflow.common.constant.KafkaTopics;
import com.urbanflow.events.alert.AlertTriggeredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class KafkaAlertEventPublisher implements AlertEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaAlertEventPublisher.class);

    private final KafkaTemplate<String, AlertTriggeredEvent> kafkaTemplate;

    public KafkaAlertEventPublisher(KafkaTemplate<String, AlertTriggeredEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(AlertTriggeredEvent event) {
        kafkaTemplate.send(KafkaTopics.ALERT_EVENTS, event.alertType(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish alert event {}", event.alertId(), ex);
                    } else {
                        log.info("Published alert event {} to {}", event.alertId(), KafkaTopics.ALERT_EVENTS);
                    }
                });
    }
}
