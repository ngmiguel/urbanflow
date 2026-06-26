package com.urbanflow.traffic.domain.model;

import com.urbanflow.common.enums.CongestionLevel;

import java.time.Instant;
import java.util.UUID;

public class TrafficSegment {

    private UUID id;
    private String name;
    private String zoneId;
    private CongestionLevel congestionLevel;
    private double averageSpeedKmh;
    private int vehicleCount;
    private double latitude;
    private double longitude;
    private Instant createdAt;
    private Instant updatedAt;

    public TrafficSegment() {
    }

    public TrafficSegment(
            UUID id,
            String name,
            String zoneId,
            CongestionLevel congestionLevel,
            double averageSpeedKmh,
            int vehicleCount,
            double latitude,
            double longitude,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.zoneId = zoneId;
        this.congestionLevel = congestionLevel;
        this.averageSpeedKmh = averageSpeedKmh;
        this.vehicleCount = vehicleCount;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static TrafficSegment create(
            String name,
            String zoneId,
            CongestionLevel congestionLevel,
            double averageSpeedKmh,
            int vehicleCount,
            double latitude,
            double longitude
    ) {
        Instant now = Instant.now();
        return new TrafficSegment(
                UUID.randomUUID(),
                name,
                zoneId,
                congestionLevel,
                averageSpeedKmh,
                vehicleCount,
                latitude,
                longitude,
                now,
                now
        );
    }

    public void updateMetrics(
            CongestionLevel congestionLevel,
            double averageSpeedKmh,
            int vehicleCount
    ) {
        this.congestionLevel = congestionLevel;
        this.averageSpeedKmh = averageSpeedKmh;
        this.vehicleCount = vehicleCount;
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public CongestionLevel getCongestionLevel() {
        return congestionLevel;
    }

    public void setCongestionLevel(CongestionLevel congestionLevel) {
        this.congestionLevel = congestionLevel;
    }

    public double getAverageSpeedKmh() {
        return averageSpeedKmh;
    }

    public void setAverageSpeedKmh(double averageSpeedKmh) {
        this.averageSpeedKmh = averageSpeedKmh;
    }

    public int getVehicleCount() {
        return vehicleCount;
    }

    public void setVehicleCount(int vehicleCount) {
        this.vehicleCount = vehicleCount;
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
