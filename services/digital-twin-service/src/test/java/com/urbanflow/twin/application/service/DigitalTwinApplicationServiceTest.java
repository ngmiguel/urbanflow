package com.urbanflow.twin.application.service;

import com.urbanflow.common.enums.CongestionLevel;
import com.urbanflow.events.EventMetadata;
import com.urbanflow.events.incident.IncidentEvent;
import com.urbanflow.events.traffic.TrafficEventType;
import com.urbanflow.events.traffic.TrafficUpdateEvent;
import com.urbanflow.twin.application.port.EventIdempotencyStore;
import com.urbanflow.twin.application.port.TwinStateStore;
import com.urbanflow.twin.domain.model.TwinSimulation;
import com.urbanflow.twin.domain.model.TwinZoneState;
import com.urbanflow.twin.domain.model.WhatIfScenarioType;
import com.urbanflow.twin.domain.repository.TwinSimulationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DigitalTwinApplicationServiceTest {

    @Mock
    private TwinStateStore twinStateStore;

    @Mock
    private TwinSimulationRepository twinSimulationRepository;

    @Mock
    private WhatIfEngine whatIfEngine;

    @Mock
    private EventIdempotencyStore idempotencyStore;

    @InjectMocks
    private DigitalTwinApplicationService digitalTwinApplicationService;

    @Test
    void shouldReturnBaselineWhenZoneStateMissing() {
        when(twinStateStore.getZoneState("zone-casa-centre")).thenReturn(Optional.empty());

        TwinZoneState state = digitalTwinApplicationService.getZoneState("zone-casa-centre");

        assertEquals("zone-casa-centre", state.zoneId());
        assertEquals(CongestionLevel.FREE_FLOW, state.congestionLevel());
    }

    @Test
    void shouldRunWhatIfSimulationAndPersistState() {
        TwinZoneState baseline = TwinZoneState.baseline("zone-casa-centre");
        TwinSimulation simulation = new TwinSimulation();
        TwinZoneState projected = TwinZoneState.baseline("zone-casa-centre");

        when(twinStateStore.getZoneState("zone-casa-centre")).thenReturn(Optional.of(baseline));
        when(whatIfEngine.simulate(
                baseline,
                WhatIfScenarioType.ROAD_CLOSURE,
                null,
                60
        )).thenReturn(simulation);
        when(twinSimulationRepository.save(simulation)).thenReturn(simulation);
        when(whatIfEngine.toProjectedState(simulation)).thenReturn(projected);

        TwinSimulation result = digitalTwinApplicationService.runWhatIfSimulation(
                "zone-casa-centre",
                WhatIfScenarioType.ROAD_CLOSURE,
                null,
                60
        );

        assertEquals(simulation, result);
        verify(twinStateStore).saveProjectedState("zone-casa-centre", projected);
    }

    @Test
    void shouldSkipDuplicateTrafficEvents() {
        UUID eventId = UUID.randomUUID();
        when(idempotencyStore.alreadyProcessed(eventId)).thenReturn(true);

        TrafficUpdateEvent event = new TrafficUpdateEvent(
                EventMetadata.create("TRAFFIC_UPDATED", "traffic-service", "corr-1"),
                UUID.randomUUID(),
                "Avenue Mohammed V",
                TrafficEventType.CONGESTION_CHANGED,
                CongestionLevel.HEAVY,
                18.5,
                420,
                "zone-casa-centre"
        );

        digitalTwinApplicationService.ingestTrafficEvent(
                new TrafficUpdateEvent(
                        new EventMetadata(
                                eventId,
                                event.metadata().eventType(),
                                event.metadata().source(),
                                event.metadata().occurredAt(),
                                event.metadata().correlationId(),
                                event.metadata().schemaVersion()
                        ),
                        event.segmentId(),
                        event.segmentName(),
                        event.eventType(),
                        event.congestionLevel(),
                        event.averageSpeedKmh(),
                        event.vehicleCount(),
                        event.zoneId()
                )
        );

        verify(twinStateStore, never()).saveZoneState(any());
    }

    @Test
    void shouldIncrementActiveIncidentsOnReportedEvent() {
        UUID eventId = UUID.randomUUID();
        when(idempotencyStore.alreadyProcessed(eventId)).thenReturn(false);
        when(twinStateStore.getZoneState("zone-casa-centre"))
                .thenReturn(Optional.of(TwinZoneState.baseline("zone-casa-centre")));

        IncidentEvent event = new IncidentEvent(
                new EventMetadata(
                        eventId,
                        "INCIDENT_REPORTED",
                        "incident-service",
                        java.time.Instant.now(),
                        "corr-1",
                        1
                ),
                UUID.randomUUID(),
                IncidentEvent.IncidentEventType.REPORTED,
                "HIGH",
                "Accident",
                33.57,
                -7.58,
                "zone-casa-centre"
        );

        digitalTwinApplicationService.ingestIncidentEvent(event);

        verify(twinStateStore).saveZoneState(any(TwinZoneState.class));
        verify(idempotencyStore).markProcessed(eventId);
    }
}
