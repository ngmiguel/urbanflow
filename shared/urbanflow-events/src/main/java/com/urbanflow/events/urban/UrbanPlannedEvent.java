package com.urbanflow.events.urban;

import com.urbanflow.events.DomainEvent;
import com.urbanflow.events.EventMetadata;

import java.time.Instant;
import java.util.UUID;

/**
 * Planned urban event lifecycle published on {@code urbanflow.urban.events} (Phase 3).
 */
public record UrbanPlannedEvent(
        EventMetadata metadata,
        UUID eventId,
        UrbanEventLifecycleType lifecycleType,
        String eventType,
        String title,
        String description,
        String zoneId,
        double latitude,
        double longitude,
        Instant startsAt,
        Instant endsAt,
        int expectedAttendance
) implements DomainEvent {

    public enum UrbanEventLifecycleType {
        SCHEDULED,
        UPDATED,
        CANCELLED,
        COMPLETED
    }
}
