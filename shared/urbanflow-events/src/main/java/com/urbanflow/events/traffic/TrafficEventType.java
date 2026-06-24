package com.urbanflow.events.traffic;

/**
 * Types of traffic domain events published on {@code urbanflow.traffic.updates}.
 */
public enum TrafficEventType {
    SEGMENT_CREATED,
    SEGMENT_UPDATED,
    CONGESTION_CHANGED
}
