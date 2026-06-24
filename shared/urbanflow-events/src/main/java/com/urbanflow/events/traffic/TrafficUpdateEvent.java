package com.urbanflow.events.traffic;

import com.urbanflow.common.enums.CongestionLevel;
import com.urbanflow.events.DomainEvent;
import com.urbanflow.events.EventMetadata;

import java.util.UUID;

/**
 * Published when a traffic segment state changes (MVP core event).
 */
public record TrafficUpdateEvent(
        EventMetadata metadata,
        UUID segmentId,
        String segmentName,
        TrafficEventType eventType,
        CongestionLevel congestionLevel,
        double averageSpeedKmh,
        int vehicleCount,
        String zoneId
) implements DomainEvent {
}
