package com.urbanflow.twin.application.service;

import com.urbanflow.common.enums.CongestionLevel;
import com.urbanflow.common.exception.ResourceNotFoundException;
import com.urbanflow.events.incident.IncidentEvent;
import com.urbanflow.events.traffic.TrafficUpdateEvent;
import com.urbanflow.twin.application.port.EventIdempotencyStore;
import com.urbanflow.twin.application.port.TwinStateStore;
import com.urbanflow.twin.domain.model.TwinSimulation;
import com.urbanflow.twin.domain.model.TwinZoneState;
import com.urbanflow.twin.domain.model.WhatIfScenarioType;
import com.urbanflow.twin.domain.repository.TwinSimulationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DigitalTwinApplicationService {

    private final TwinStateStore twinStateStore;
    private final TwinSimulationRepository twinSimulationRepository;
    private final WhatIfEngine whatIfEngine;
    private final EventIdempotencyStore idempotencyStore;

    public DigitalTwinApplicationService(
            TwinStateStore twinStateStore,
            TwinSimulationRepository twinSimulationRepository,
            WhatIfEngine whatIfEngine,
            EventIdempotencyStore idempotencyStore
    ) {
        this.twinStateStore = twinStateStore;
        this.twinSimulationRepository = twinSimulationRepository;
        this.whatIfEngine = whatIfEngine;
        this.idempotencyStore = idempotencyStore;
    }

    public TwinZoneState getZoneState(String zoneId) {
        return twinStateStore.getZoneState(zoneId)
                .orElseGet(() -> TwinZoneState.baseline(zoneId));
    }

    public TwinSimulation runWhatIfSimulation(
            String zoneId,
            WhatIfScenarioType scenarioType,
            String incidentSeverity,
            Integer closureDurationMinutes
    ) {
        TwinZoneState baseline = getZoneState(zoneId);
        TwinSimulation simulation = whatIfEngine.simulate(
                baseline,
                scenarioType,
                incidentSeverity,
                closureDurationMinutes
        );
        TwinSimulation saved = twinSimulationRepository.save(simulation);
        twinStateStore.saveProjectedState(zoneId, whatIfEngine.toProjectedState(saved));
        return saved;
    }

    public TwinSimulation getSimulation(UUID id) {
        return twinSimulationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Simulation", id));
    }

    public List<TwinSimulation> listSimulations(String zoneId, int limit) {
        return twinSimulationRepository.findByZoneId(zoneId, limit);
    }

    @Transactional
    public void ingestTrafficEvent(TrafficUpdateEvent event) {
        if (shouldSkip(event.metadata().eventId())) {
            return;
        }

        TwinZoneState current = twinStateStore.getZoneState(event.zoneId())
                .orElseGet(() -> TwinZoneState.baseline(event.zoneId()));

        TwinZoneState updated = new TwinZoneState(
                event.zoneId(),
                event.congestionLevel(),
                event.averageSpeedKmh(),
                event.vehicleCount(),
                current.activeIncidents(),
                event.metadata().occurredAt()
        );
        twinStateStore.saveZoneState(updated);
        idempotencyStore.markProcessed(event.metadata().eventId());
    }

    @Transactional
    public void ingestIncidentEvent(IncidentEvent event) {
        if (shouldSkip(event.metadata().eventId())) {
            return;
        }

        TwinZoneState current = twinStateStore.getZoneState(event.zoneId())
                .orElseGet(() -> TwinZoneState.baseline(event.zoneId()));

        int activeIncidents = current.activeIncidents();
        if (event.eventType() == IncidentEvent.IncidentEventType.REPORTED) {
            activeIncidents++;
        } else if (event.eventType() == IncidentEvent.IncidentEventType.RESOLVED && activeIncidents > 0) {
            activeIncidents--;
        }

        CongestionLevel congestion = activeIncidents >= 2
                ? escalate(current.congestionLevel())
                : current.congestionLevel();

        TwinZoneState updated = new TwinZoneState(
                event.zoneId(),
                congestion,
                Math.max(8.0, current.averageSpeedKmh() * (activeIncidents > 0 ? 0.85 : 1.0)),
                current.vehicleCount(),
                activeIncidents,
                event.metadata().occurredAt()
        );
        twinStateStore.saveZoneState(updated);
        idempotencyStore.markProcessed(event.metadata().eventId());
    }

    private boolean shouldSkip(UUID eventId) {
        if (idempotencyStore.alreadyProcessed(eventId)) {
            return true;
        }
        return false;
    }

    private CongestionLevel escalate(CongestionLevel current) {
        CongestionLevel[] levels = CongestionLevel.values();
        int index = Math.min(levels.length - 1, current.ordinal() + 1);
        return levels[index];
    }
}
