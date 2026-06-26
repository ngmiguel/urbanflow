package com.urbanflow.incident.domain.model;

import com.urbanflow.common.exception.BusinessException;

import java.time.Instant;
import java.util.UUID;

public class Incident {

    private UUID id;
    private IncidentType type;
    private IncidentSeverity severity;
    private IncidentStatus status;
    private String description;
    private String zoneId;
    private double latitude;
    private double longitude;
    private String reportedBy;
    private Instant resolvedAt;
    private Instant createdAt;
    private Instant updatedAt;

    public Incident() {
    }

    public Incident(
            UUID id,
            IncidentType type,
            IncidentSeverity severity,
            IncidentStatus status,
            String description,
            String zoneId,
            double latitude,
            double longitude,
            String reportedBy,
            Instant resolvedAt,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.type = type;
        this.severity = severity;
        this.status = status;
        this.description = description;
        this.zoneId = zoneId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.reportedBy = reportedBy;
        this.resolvedAt = resolvedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Incident report(
            IncidentType type,
            IncidentSeverity severity,
            String description,
            String zoneId,
            double latitude,
            double longitude,
            String reportedBy
    ) {
        Instant now = Instant.now();
        return new Incident(
                UUID.randomUUID(),
                type,
                severity,
                IncidentStatus.OPEN,
                description,
                zoneId,
                latitude,
                longitude,
                reportedBy,
                null,
                now,
                now
        );
    }

    public void update(IncidentSeverity severity, String description, IncidentStatus status) {
        ensureNotResolved();
        this.severity = severity;
        this.description = description;
        if (status != null) {
            this.status = status;
        }
        this.updatedAt = Instant.now();
    }

    public void resolve() {
        if (status == IncidentStatus.RESOLVED) {
            throw new BusinessException("INCIDENT_ALREADY_RESOLVED", "Incident is already resolved");
        }
        this.status = IncidentStatus.RESOLVED;
        this.resolvedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    private void ensureNotResolved() {
        if (status == IncidentStatus.RESOLVED) {
            throw new BusinessException("INCIDENT_ALREADY_RESOLVED", "Cannot modify a resolved incident");
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public IncidentType getType() {
        return type;
    }

    public void setType(IncidentType type) {
        this.type = type;
    }

    public IncidentSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(IncidentSeverity severity) {
        this.severity = severity;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public void setStatus(IncidentStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Instant resolvedAt) {
        this.resolvedAt = resolvedAt;
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
