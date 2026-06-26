package com.urbanflow.alert.domain.model;

import com.urbanflow.common.exception.BusinessException;

import java.time.Instant;
import java.util.UUID;

public class Alert {

    private UUID id;
    private UUID ruleId;
    private String alertType;
    private AlertSeverity severity;
    private String message;
    private String zoneId;
    private AlertStatus status;
    private String sourceEventId;
    private Instant createdAt;
    private Instant updatedAt;

    public Alert() {
    }

    public Alert(
            UUID id,
            UUID ruleId,
            String alertType,
            AlertSeverity severity,
            String message,
            String zoneId,
            AlertStatus status,
            String sourceEventId,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.ruleId = ruleId;
        this.alertType = alertType;
        this.severity = severity;
        this.message = message;
        this.zoneId = zoneId;
        this.status = status;
        this.sourceEventId = sourceEventId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Alert trigger(
            UUID ruleId,
            String alertType,
            AlertSeverity severity,
            String message,
            String zoneId,
            String sourceEventId
    ) {
        Instant now = Instant.now();
        Alert alert = new Alert();
        alert.id = UUID.randomUUID();
        alert.ruleId = ruleId;
        alert.alertType = alertType;
        alert.severity = severity;
        alert.message = message;
        alert.zoneId = zoneId;
        alert.status = AlertStatus.ACTIVE;
        alert.sourceEventId = sourceEventId;
        alert.createdAt = now;
        alert.updatedAt = now;
        return alert;
    }

    public void acknowledge() {
        if (status == AlertStatus.DISMISSED) {
            throw new BusinessException("ALERT_DISMISSED", "Cannot acknowledge a dismissed alert");
        }
        this.status = AlertStatus.ACKNOWLEDGED;
        this.updatedAt = Instant.now();
    }

    public void dismiss() {
        this.status = AlertStatus.DISMISSED;
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRuleId() {
        return ruleId;
    }

    public void setRuleId(UUID ruleId) {
        this.ruleId = ruleId;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public AlertSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(AlertSeverity severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public void setStatus(AlertStatus status) {
        this.status = status;
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

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
