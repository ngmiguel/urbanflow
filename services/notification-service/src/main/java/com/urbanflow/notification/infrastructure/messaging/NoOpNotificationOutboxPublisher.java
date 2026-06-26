package com.urbanflow.notification.infrastructure.messaging;

import com.urbanflow.notification.application.port.NotificationOutboxPublisher;
import com.urbanflow.events.notification.NotificationOutboxEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class NoOpNotificationOutboxPublisher implements NotificationOutboxPublisher {

    @Override
    public void publish(NotificationOutboxEvent event) {
    }
}
