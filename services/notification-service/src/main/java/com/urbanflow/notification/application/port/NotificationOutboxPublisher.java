package com.urbanflow.notification.application.port;

import com.urbanflow.events.notification.NotificationOutboxEvent;

public interface NotificationOutboxPublisher {

    void publish(NotificationOutboxEvent event);
}
