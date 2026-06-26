package com.urbanflow.environment.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SensorReadingTest {

    @Test
    void shouldCreateReadingFromTelemetry() {
        SensorReading reading = SensorReading.fromTelemetry(
                "sensor-01",
                EnvironmentSensorType.AIR_QUALITY.name(),
                42.0,
                "AQI",
                "zone-casa-centre",
                33.57,
                -7.58,
                "evt-1",
                Instant.now()
        );

        assertNotNull(reading.getId());
        assertEquals("sensor-01", reading.getDeviceId());
        assertEquals(42.0, reading.getValue());
    }

    @Test
    void shouldDetectEnvironmentSensors() {
        assertTrue(SensorReading.isEnvironmentSensor("AIR_QUALITY"));
        assertTrue(SensorReading.isEnvironmentSensor("environment"));
        assertFalse(SensorReading.isEnvironmentSensor("TRAFFIC"));
    }
}
