package com.urbanflow.websocket.application.dto;

import com.urbanflow.events.notification.NotificationOutboxEvent;

import java.time.Instant;
import java.util.UUID;

public record RealtimeNotificationMessage(
        UUID notificationId,
        String userId,
        String title,
        String message,
        String severity,
        String zoneId,
        String notificationType,
        Instant occurredAt
) {

    public static RealtimeNotificationMessage from(NotificationOutboxEvent event) {
        return new RealtimeNotificationMessage(
                event.notificationId(),
                event.userId(),
                event.title(),
                event.message(),
                event.severity(),
                event.zoneId(),
                event.notificationType(),
                event.metadata().occurredAt()
        );
    }
}
