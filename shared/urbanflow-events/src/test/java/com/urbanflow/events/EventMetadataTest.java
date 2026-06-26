package com.urbanflow.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EventMetadataTest {

    @Test
    void shouldCreateMetadataWithDefaults() {
        EventMetadata metadata = EventMetadata.create("TRAFFIC_UPDATED", "traffic-service", "corr-1");

        assertNotNull(metadata.eventId());
        assertEquals("TRAFFIC_UPDATED", metadata.eventType());
        assertEquals("traffic-service", metadata.source());
        assertEquals("corr-1", metadata.correlationId());
        assertEquals(1, metadata.schemaVersion());
        assertNotNull(metadata.occurredAt());
    }
}
