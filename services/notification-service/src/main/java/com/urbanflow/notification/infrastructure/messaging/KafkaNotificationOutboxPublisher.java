package com.urbanflow.notification.infrastructure.messaging;

import com.urbanflow.notification.application.port.NotificationOutboxPublisher;
import com.urbanflow.common.constant.KafkaTopics;
import com.urbanflow.events.notification.NotificationOutboxEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class KafkaNotificationOutboxPublisher implements NotificationOutboxPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaNotificationOutboxPublisher.class);

    private final KafkaTemplate<String, NotificationOutboxEvent> kafkaTemplate;

    public KafkaNotificationOutboxPublisher(KafkaTemplate<String, NotificationOutboxEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(NotificationOutboxEvent event) {
        kafkaTemplate.send(KafkaTopics.NOTIFICATION_OUTBOX, event.userId(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish notification outbox for user {}", event.userId(), ex);
                    } else {
                        log.info("Published notification outbox to {}", KafkaTopics.NOTIFICATION_OUTBOX);
                    }
                });
    }
}
