package com.urbanflow.events.sensor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.urbanflow.events.EventMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SensorRawEventTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldSerializeAndDeserializeSensorRawEvent() throws Exception {
        SensorRawEvent event = new SensorRawEvent(
                EventMetadata.create("SENSOR_READING", "iot-device-service", "corr-7"),
                "SENSOR-01",
                "TRAFFIC",
                42.5,
                "km/h",
                33.57,
                -7.58,
                "zone-casa-centre"
        );

        String json = objectMapper.writeValueAsString(event);
        SensorRawEvent deserialized = objectMapper.readValue(json, SensorRawEvent.class);

        assertNotNull(deserialized.metadata());
        assertEquals("SENSOR-01", deserialized.deviceId());
        assertEquals(42.5, deserialized.value());
        assertEquals("zone-casa-centre", deserialized.zoneId());
    }
}
