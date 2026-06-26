package com.urbanflow.twin.infrastructure.redis;

import com.urbanflow.twin.application.port.EventIdempotencyStore;
import com.urbanflow.twin.application.port.TwinStateStore;
import com.urbanflow.twin.domain.model.TwinZoneState;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("test")
public class InMemoryTwinStateStore implements TwinStateStore, EventIdempotencyStore {

    private final Map<String, TwinZoneState> zoneStates = new ConcurrentHashMap<>();
    private final Map<UUID, Boolean> processedEvents = new ConcurrentHashMap<>();

    @Override
    public Optional<TwinZoneState> getZoneState(String zoneId) {
        return Optional.ofNullable(zoneStates.get(zoneId));
    }

    @Override
    public void saveZoneState(TwinZoneState state) {
        zoneStates.put(state.zoneId(), state);
    }

    @Override
    public void saveProjectedState(String zoneId, TwinZoneState projectedState) {
        zoneStates.put(zoneId + ":projection", projectedState);
    }

    @Override
    public boolean alreadyProcessed(UUID eventId) {
        return processedEvents.containsKey(eventId);
    }

    @Override
    public void markProcessed(UUID eventId) {
        processedEvents.put(eventId, Boolean.TRUE);
    }
}
