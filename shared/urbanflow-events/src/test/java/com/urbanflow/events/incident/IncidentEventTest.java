package com.urbanflow.events.incident;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.urbanflow.events.EventMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class IncidentEventTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldSerializeAndDeserializeIncidentEvent() throws Exception {
        UUID incidentId = UUID.randomUUID();
        IncidentEvent event = new IncidentEvent(
                EventMetadata.create("INCIDENT_REPORTED", "incident-service", "corr-1"),
                incidentId,
                IncidentEvent.IncidentEventType.REPORTED,
                "HIGH",
                "Multi-vehicle collision",
                33.57,
                -7.58,
                "zone-casa-centre"
        );

        String json = objectMapper.writeValueAsString(event);
        IncidentEvent deserialized = objectMapper.readValue(json, IncidentEvent.class);

        assertNotNull(deserialized.metadata());
        assertEquals(incidentId, deserialized.incidentId());
        assertEquals(IncidentEvent.IncidentEventType.REPORTED, deserialized.eventType());
        assertEquals("zone-casa-centre", deserialized.zoneId());
    }
}
