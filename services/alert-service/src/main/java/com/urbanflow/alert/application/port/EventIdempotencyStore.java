package com.urbanflow.alert.application.port;

import java.util.UUID;

public interface EventIdempotencyStore {

    /**
     * @return true if this event was already processed
     */
    boolean alreadyProcessed(UUID eventId);

    void markProcessed(UUID eventId);
}
