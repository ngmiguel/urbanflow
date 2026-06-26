package com.urbanflow.common.constant;

/**
 * Kafka topic names used across UrbanFlow microservices.
 */
public final class KafkaTopics {

    public static final String TRAFFIC_UPDATES = "urbanflow.traffic.updates";
    public static final String SENSOR_RAW = "urbanflow.sensor.raw";
    public static final String INCIDENT_EVENTS = "urbanflow.incident.events";
    public static final String ALERT_EVENTS = "urbanflow.alert.events";
    public static final String NOTIFICATION_OUTBOX = "urbanflow.notification.outbox";
    public static final String ANOMALY_DETECTED = "urbanflow.anomaly.detected";
    public static final String URBAN_EVENTS = "urbanflow.urban.events";
    public static final String DEAD_LETTER = "urbanflow.dead-letter";

    private KafkaTopics() {
    }
}
