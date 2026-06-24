package com.urbanflow.events.traffic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.urbanflow.common.enums.CongestionLevel;
import com.urbanflow.events.EventMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TrafficUpdateEventTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldSerializeAndDeserializeTrafficUpdateEvent() throws Exception {
        TrafficUpdateEvent event = new TrafficUpdateEvent(
                EventMetadata.create("TRAFFIC_UPDATED", "traffic-service", "corr-123"),
                UUID.randomUUID(),
                "Avenue Mohammed V",
                TrafficEventType.CONGESTION_CHANGED,
                CongestionLevel.HEAVY,
                18.5,
                420,
                "zone-casablanca-centre"
        );

        String json = objectMapper.writeValueAsString(event);
        TrafficUpdateEvent deserialized = objectMapper.readValue(json, TrafficUpdateEvent.class);

        assertNotNull(deserialized.metadata());
        assertEquals(event.segmentName(), deserialized.segmentName());
        assertEquals(CongestionLevel.HEAVY, deserialized.congestionLevel());
    }
}
