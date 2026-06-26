package com.urbanflow.notification.infrastructure.redis;

import com.urbanflow.notification.application.port.EventIdempotencyStore;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("test")
public class InMemoryEventIdempotencyStore implements EventIdempotencyStore {

    private final Set<UUID> processed = ConcurrentHashMap.newKeySet();

    @Override
    public boolean alreadyProcessed(UUID eventId) {
        return processed.contains(eventId);
    }

    @Override
    public void markProcessed(UUID eventId) {
        processed.add(eventId);
    }
}
