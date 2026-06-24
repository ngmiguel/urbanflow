package com.urbanflow.events.alert;

import com.urbanflow.events.DomainEvent;
import com.urbanflow.events.EventMetadata;

import java.util.UUID;

/**
 * Alert triggered by rule correlation, published on {@code urbanflow.alert.events} (Phase 2).
 */
public record AlertTriggeredEvent(
        EventMetadata metadata,
        UUID alertId,
        String alertType,
        String severity,
        String message,
        String zoneId,
        String sourceEventId
) implements DomainEvent {
}
