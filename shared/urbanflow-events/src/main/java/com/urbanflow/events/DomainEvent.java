package com.urbanflow.events;

import com.urbanflow.events.alert.AlertTriggeredEvent;
import com.urbanflow.events.alert.AnomalyDetectedEvent;
import com.urbanflow.events.incident.IncidentEvent;
import com.urbanflow.events.sensor.SensorRawEvent;
import com.urbanflow.events.traffic.TrafficUpdateEvent;

/**
 * Marker for all Kafka domain events published across UrbanFlow.
 */
public sealed interface DomainEvent permits
        TrafficUpdateEvent,
        SensorRawEvent,
        IncidentEvent,
        AlertTriggeredEvent,
        AnomalyDetectedEvent {

    EventMetadata metadata();
}
