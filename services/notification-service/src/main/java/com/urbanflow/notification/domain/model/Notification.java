package com.urbanflow.notification.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Notification {

    private UUID id;
    private String userId;
    private NotificationType type;
    private NotificationStatus status;
    private String title;
    private String message;
    private String severity;
    private String zoneId;
    private String sourceEventId;
    private Instant createdAt;
    private Instant readAt;

    public Notification() {
    }

    public Notification(
            UUID id,
            String userId,
            NotificationType type,
            NotificationStatus status,
            String title,
            String message,
            String severity,
            String zoneId,
            String sourceEventId,
            Instant createdAt,
            Instant readAt
    ) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.status = status;
        this.title = title;
        this.message = message;
        this.severity = severity;
        this.zoneId = zoneId;
        this.sourceEventId = sourceEventId;
        this.createdAt = createdAt;
        this.readAt = readAt;
    }

    public static Notification create(
            String userId,
            NotificationType type,
            String title,
            String message,
            String severity,
            String zoneId,
            String sourceEventId
    ) {
        return new Notification(
                UUID.randomUUID(),
                userId,
                type,
                NotificationStatus.UNREAD,
                title,
                message,
                severity,
                zoneId,
                sourceEventId,
                Instant.now(),
                null
        );
    }

    public void markRead() {
        this.status = NotificationStatus.READ;
        this.readAt = Instant.now();
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

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getSourceEventId() {
        return sourceEventId;
    }

    public void setSourceEventId(String sourceEventId) {
        this.sourceEventId = sourceEventId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getReadAt() {
        return readAt;
    }

    public void setReadAt(Instant readAt) {
        this.readAt = readAt;
    }
}
