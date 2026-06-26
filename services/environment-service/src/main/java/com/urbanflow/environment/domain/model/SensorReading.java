package com.urbanflow.environment.domain.model;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class SensorReading {

    private static final Set<String> ENVIRONMENT_SENSOR_TYPES = Set.of(
            EnvironmentSensorType.ENVIRONMENT.name(),
            EnvironmentSensorType.AIR_QUALITY.name(),
            EnvironmentSensorType.NOISE.name()
    );

    private UUID id;
    private String deviceId;
    private String sensorType;
    private double value;
    private String unit;
    private String zoneId;
    private double latitude;
    private double longitude;
    private String sourceEventId;
    private Instant recordedAt;

    public SensorReading() {
    }

    public SensorReading(
            UUID id,
            String deviceId,
            String sensorType,
            double value,
            String unit,
            String zoneId,
            double latitude,
            double longitude,
            String sourceEventId,
            Instant recordedAt
    ) {
        this.id = id;
        this.deviceId = deviceId;
        this.sensorType = sensorType;
        this.value = value;
        this.unit = unit;
        this.zoneId = zoneId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sourceEventId = sourceEventId;
        this.recordedAt = recordedAt;
    }

    public static SensorReading fromTelemetry(
            String deviceId,
            String sensorType,
            double value,
            String unit,
            String zoneId,
            double latitude,
            double longitude,
            String sourceEventId,
            Instant recordedAt
    ) {
        return new SensorReading(
                UUID.randomUUID(),
                deviceId,
                sensorType,
                value,
                unit,
                zoneId,
                latitude,
                longitude,
                sourceEventId,
                recordedAt
        );
    }

    public static boolean isEnvironmentSensor(String sensorType) {
        return sensorType != null && ENVIRONMENT_SENSOR_TYPES.contains(sensorType.toUpperCase());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSourceEventId() {
        return sourceEventId;
    }

    public void setSourceEventId(String sourceEventId) {
        this.sourceEventId = sourceEventId;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(Instant recordedAt) {
        this.recordedAt = recordedAt;
    }
}
