package com.urbanflow.events;

/**
 * Marker for all Kafka domain events published across UrbanFlow.
 */
public interface DomainEvent {

    EventMetadata metadata();
}
