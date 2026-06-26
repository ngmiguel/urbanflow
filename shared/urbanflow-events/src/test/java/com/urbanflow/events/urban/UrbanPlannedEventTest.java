package com.urbanflow.events.urban;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.urbanflow.events.EventMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UrbanPlannedEventTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldSerializeAndDeserializeUrbanPlannedEvent() throws Exception {
        Instant startsAt = Instant.now().plus(1, ChronoUnit.DAYS);
        Instant endsAt = startsAt.plus(2, ChronoUnit.HOURS);
        UUID eventId = UUID.randomUUID();

        UrbanPlannedEvent event = new UrbanPlannedEvent(
                EventMetadata.create("SCHEDULED", "event-service", "corr-3"),
                eventId,
                UrbanPlannedEvent.UrbanEventLifecycleType.SCHEDULED,
                "CONCERT",
                "Jazz Night",
                "Open-air concert",
                "zone-casa-centre",
                33.57,
                -7.58,
                startsAt,
                endsAt,
                5000
        );

        String json = objectMapper.writeValueAsString(event);
        UrbanPlannedEvent deserialized = objectMapper.readValue(json, UrbanPlannedEvent.class);

        assertNotNull(deserialized.metadata());
        assertEquals(eventId, deserialized.eventId());
        assertEquals(UrbanPlannedEvent.UrbanEventLifecycleType.SCHEDULED, deserialized.lifecycleType());
        assertEquals("Jazz Night", deserialized.title());
    }
}
