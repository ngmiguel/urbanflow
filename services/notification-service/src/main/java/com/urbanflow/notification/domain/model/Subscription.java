package com.urbanflow.notification.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Subscription {

    private UUID id;
    private String userId;
    private String zoneId;
    private boolean enabled;
    private Instant createdAt;

    public Subscription() {
    }

    public Subscription(UUID id, String userId, String zoneId, boolean enabled, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.zoneId = zoneId;
        this.enabled = enabled;
        this.createdAt = createdAt;
    }

    public static Subscription create(String userId, String zoneId) {
        return new Subscription(UUID.randomUUID(), userId, zoneId, true, Instant.now());
    }

    public boolean matchesZone(String eventZoneId) {
        return zoneId == null || zoneId.isBlank() || zoneId.equals(eventZoneId);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
