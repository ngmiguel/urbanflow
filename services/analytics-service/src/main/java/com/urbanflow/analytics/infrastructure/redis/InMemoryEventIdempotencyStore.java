package com.urbanflow.analytics.infrastructure.redis;

import com.urbanflow.analytics.application.port.EventIdempotencyStore;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("test")
public class InMemoryEventIdempotencyStore implements EventIdempotencyStore {

    private final Map<UUID, Boolean> processedEvents = new ConcurrentHashMap<>();

    @Override
    public boolean alreadyProcessed(UUID eventId) {
        return processedEvents.containsKey(eventId);
    }

    @Override
    public void markProcessed(UUID eventId) {
        processedEvents.put(eventId, Boolean.TRUE);
    }
}
