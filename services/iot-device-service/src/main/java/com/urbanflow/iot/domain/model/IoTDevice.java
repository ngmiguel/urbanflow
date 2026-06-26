package com.urbanflow.iot.domain.model;

import com.urbanflow.common.exception.BusinessException;

import java.time.Instant;

public class IoTDevice {

    private String deviceId;
    private String name;
    private SensorType sensorType;
    private String zoneId;
    private double latitude;
    private double longitude;
    private DeviceStatus status;
    private Instant lastHeartbeatAt;
    private Instant createdAt;
    private Instant updatedAt;

    public IoTDevice() {
    }

    public IoTDevice(
            String deviceId,
            String name,
            SensorType sensorType,
            String zoneId,
            double latitude,
            double longitude,
            DeviceStatus status,
            Instant lastHeartbeatAt,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.deviceId = deviceId;
        this.name = name;
        this.sensorType = sensorType;
        this.zoneId = zoneId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.lastHeartbeatAt = lastHeartbeatAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static IoTDevice register(
            String deviceId,
            String name,
            SensorType sensorType,
            String zoneId,
            double latitude,
            double longitude
    ) {
        Instant now = Instant.now();
        return new IoTDevice(
                deviceId.toUpperCase(),
                name,
                sensorType,
                zoneId,
                latitude,
                longitude,
                DeviceStatus.OFFLINE,
                null,
                now,
                now
        );
    }

    public void heartbeat() {
        this.status = DeviceStatus.ONLINE;
        this.lastHeartbeatAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void markOffline() {
        this.status = DeviceStatus.OFFLINE;
        this.updatedAt = Instant.now();
    }

    public void ensureOnline() {
        if (status != DeviceStatus.ONLINE) {
            throw new BusinessException("DEVICE_OFFLINE", "Device must be online to submit telemetry");
        }
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
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

    public DeviceStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    public Instant getLastHeartbeatAt() {
        return lastHeartbeatAt;
    }

    public void setLastHeartbeatAt(Instant lastHeartbeatAt) {
        this.lastHeartbeatAt = lastHeartbeatAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
