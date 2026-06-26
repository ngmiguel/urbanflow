package com.urbanflow.twin.application.port;

import java.util.UUID;

public interface EventIdempotencyStore {

    boolean alreadyProcessed(UUID eventId);

    void markProcessed(UUID eventId);
}
