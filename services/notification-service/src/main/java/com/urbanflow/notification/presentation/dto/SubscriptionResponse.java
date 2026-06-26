package com.urbanflow.notification.presentation.dto;

import com.urbanflow.notification.application.dto.SubscriptionDto;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public record SubscriptionResponse(
        UUID id,
        String userId,
        String zoneId,
        boolean enabled,
        Instant createdAt
) {

    public static SubscriptionResponse from(SubscriptionDto dto) {
        return new SubscriptionResponse(
                dto.id(),
                dto.userId(),
                dto.zoneId(),
                dto.enabled(),
                dto.createdAt()
        );
    }
}
