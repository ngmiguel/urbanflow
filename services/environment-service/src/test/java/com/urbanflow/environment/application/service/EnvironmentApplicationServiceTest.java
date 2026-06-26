package com.urbanflow.environment.application.service;

import com.urbanflow.environment.application.port.EventIdempotencyStore;
import com.urbanflow.environment.domain.model.SensorReading;
import com.urbanflow.environment.domain.repository.SensorReadingRepository;
import com.urbanflow.events.EventMetadata;
import com.urbanflow.events.sensor.SensorRawEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnvironmentApplicationServiceTest {

    @Mock
    private SensorReadingRepository sensorReadingRepository;

    @Mock
    private EventIdempotencyStore idempotencyStore;

    @InjectMocks
    private EnvironmentApplicationService environmentApplicationService;

    @Test
    void shouldIngestEnvironmentSensorEvent() {
        UUID eventId = UUID.randomUUID();
        when(idempotencyStore.alreadyProcessed(eventId)).thenReturn(false);
        when(sensorReadingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        SensorRawEvent event = new SensorRawEvent(
                new EventMetadata(eventId, "SENSOR_READING", "iot-device-service", Instant.now(), "corr-1", 1),
                "ENV-001",
                "AIR_QUALITY",
                45.0,
                "ug/m3",
                33.57,
                -7.58,
                "zone-casa-centre"
        );

        environmentApplicationService.ingestSensorEvent(event);

        verify(sensorReadingRepository).save(any(SensorReading.class));
        verify(idempotencyStore).markProcessed(eventId);
    }

    @Test
    void shouldSkipNonEnvironmentSensorTypes() {
        UUID eventId = UUID.randomUUID();
        when(idempotencyStore.alreadyProcessed(eventId)).thenReturn(false);

        SensorRawEvent event = new SensorRawEvent(
                new EventMetadata(eventId, "SENSOR_READING", "iot-device-service", Instant.now(), "corr-1", 1),
                "TRAFFIC-001",
                "TRAFFIC",
                120.0,
                "veh/h",
                33.57,
                -7.58,
                "zone-casa-centre"
        );

        environmentApplicationService.ingestSensorEvent(event);

        verify(sensorReadingRepository, never()).save(any());
        verify(idempotencyStore).markProcessed(eventId);
    }
}
