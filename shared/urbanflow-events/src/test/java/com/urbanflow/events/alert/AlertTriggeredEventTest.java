package com.urbanflow.events.alert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.urbanflow.events.EventMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AlertTriggeredEventTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldSerializeAndDeserializeAlertTriggeredEvent() throws Exception {
        UUID alertId = UUID.randomUUID();
        AlertTriggeredEvent event = new AlertTriggeredEvent(
                EventMetadata.create("ALERT_TRIGGERED", "alert-service", "corr-9"),
                alertId,
                "TRAFFIC_ANOMALY",
                "HIGH",
                "Speed dropped below threshold",
                "zone-casa-centre",
                "evt-123"
        );

        String json = objectMapper.writeValueAsString(event);
        AlertTriggeredEvent deserialized = objectMapper.readValue(json, AlertTriggeredEvent.class);

        assertNotNull(deserialized.metadata());
        assertEquals(alertId, deserialized.alertId());
        assertEquals("TRAFFIC_ANOMALY", deserialized.alertType());
        assertEquals("zone-casa-centre", deserialized.zoneId());
    }
}
