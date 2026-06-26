package com.urbanflow.events.notification;

import com.urbanflow.events.DomainEvent;
import com.urbanflow.events.EventMetadata;

import java.util.UUID;

/**
 * Notification dispatch event published on {@code urbanflow.notification.outbox} (Phase 2).
 */
public record NotificationOutboxEvent(
        EventMetadata metadata,
        UUID notificationId,
        String userId,
        String stompTopic,
        String title,
        String message,
        String severity,
        String zoneId,
        String notificationType
) implements DomainEvent {
}
