package com.urbanflow.notification.presentation.dto;

import jakarta.validation.constraints.Size;

public record CreateSubscriptionRequest(
        @Size(max = 100) String zoneId
) {
}
