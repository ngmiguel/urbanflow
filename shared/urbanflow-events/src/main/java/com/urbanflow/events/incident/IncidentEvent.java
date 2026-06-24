package com.urbanflow.events.incident;

import com.urbanflow.events.DomainEvent;
import com.urbanflow.events.EventMetadata;

import java.util.UUID;

/**
 * Road incident lifecycle event published on {@code urbanflow.incident.events} (Phase 2).
 */
public record IncidentEvent(
        EventMetadata metadata,
        UUID incidentId,
        IncidentEventType eventType,
        String severity,
        String description,
        double latitude,
        double longitude,
        String zoneId
) implements DomainEvent {

    public enum IncidentEventType {
        REPORTED,
        UPDATED,
        RESOLVED
    }
}
