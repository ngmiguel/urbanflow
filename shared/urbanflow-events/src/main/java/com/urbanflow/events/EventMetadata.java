package com.urbanflow.events;

import com.urbanflow.common.util.IdGenerator;

import java.time.Instant;
import java.util.UUID;

/**
 * Common metadata attached to every domain event for traceability and idempotency.
 */
public record EventMetadata(
        UUID eventId,
        String eventType,
        String source,
        Instant occurredAt,
        String correlationId,
        int schemaVersion
) {

    public static EventMetadata create(String eventType, String source, String correlationId) {
        return new EventMetadata(
                IdGenerator.newId(),
                eventType,
                source,
                Instant.now(),
                correlationId,
                1
        );
    }
}
