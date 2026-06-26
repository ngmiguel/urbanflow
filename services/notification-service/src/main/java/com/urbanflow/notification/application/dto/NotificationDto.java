package com.urbanflow.notification.application.dto;

import com.urbanflow.notification.domain.model.NotificationStatus;
import com.urbanflow.notification.domain.model.NotificationType;

import java.time.Instant;
import java.util.UUID;

public record NotificationDto(
        UUID id,
        String userId,
        NotificationType type,
        NotificationStatus status,
        String title,
        String message,
        String severity,
        String zoneId,
        String sourceEventId,
        Instant createdAt,
        Instant readAt
) {
}
