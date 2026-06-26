package com.urbanflow.notification.presentation.dto;

import com.urbanflow.notification.application.dto.NotificationDto;
import com.urbanflow.notification.domain.model.NotificationStatus;
import com.urbanflow.notification.domain.model.NotificationType;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
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

    public static NotificationResponse from(NotificationDto dto) {
        return new NotificationResponse(
                dto.id(),
                dto.userId(),
                dto.type(),
                dto.status(),
                dto.title(),
                dto.message(),
                dto.severity(),
                dto.zoneId(),
                dto.sourceEventId(),
                dto.createdAt(),
                dto.readAt()
        );
    }
}
