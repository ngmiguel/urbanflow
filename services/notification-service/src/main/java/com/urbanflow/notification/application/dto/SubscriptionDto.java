package com.urbanflow.notification.application.dto;

import java.time.Instant;
import java.util.UUID;

public record SubscriptionDto(
        UUID id,
        String userId,
        String zoneId,
        boolean enabled,
        Instant createdAt
) {
}
