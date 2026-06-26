package com.urbanflow.event.domain.model;

import com.urbanflow.common.exception.BusinessException;

import java.time.Instant;
import java.util.UUID;

public class UrbanEvent {

    private UUID id;
    private UrbanEventType type;
    private UrbanEventStatus status;
    private String title;
    private String description;
    private String zoneId;
    private double latitude;
    private double longitude;
    private Instant startsAt;
    private Instant endsAt;
    private int expectedAttendance;
    private String organizer;
    private Instant createdAt;
    private Instant updatedAt;

    public UrbanEvent() {
    }

    public static UrbanEvent schedule(
            UrbanEventType type,
            String title,
            String description,
            String zoneId,
            double latitude,
            double longitude,
            Instant startsAt,
            Instant endsAt,
            int expectedAttendance,
            String organizer
    ) {
        if (endsAt.isBefore(startsAt)) {
            throw new BusinessException("INVALID_EVENT_WINDOW", "Event end time must be after start time");
        }

        Instant now = Instant.now();
        return new UrbanEvent(
                UUID.randomUUID(),
                type,
                UrbanEventStatus.SCHEDULED,
                title,
                description,
                zoneId,
                latitude,
                longitude,
                startsAt,
                endsAt,
                expectedAttendance,
                organizer,
                now,
                now
        );
    }

    public UrbanEvent(
            UUID id,
            UrbanEventType type,
            UrbanEventStatus status,
            String title,
            String description,
            String zoneId,
            double latitude,
            double longitude,
            Instant startsAt,
            Instant endsAt,
            int expectedAttendance,
            String organizer,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.title = title;
        this.description = description;
        this.zoneId = zoneId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.expectedAttendance = expectedAttendance;
        this.organizer = organizer;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void update(
            String title,
            String description,
            Instant startsAt,
            Instant endsAt,
            int expectedAttendance
    ) {
        ensureMutable();
        if (endsAt.isBefore(startsAt)) {
            throw new BusinessException("INVALID_EVENT_WINDOW", "Event end time must be after start time");
        }
        this.title = title;
        this.description = description;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.expectedAttendance = expectedAttendance;
        this.updatedAt = Instant.now();
    }

    public void cancel() {
        ensureMutable();
        this.status = UrbanEventStatus.CANCELLED;
        this.updatedAt = Instant.now();
    }

    public void complete() {
        if (status == UrbanEventStatus.CANCELLED) {
            throw new BusinessException("EVENT_CANCELLED", "Cannot complete a cancelled event");
        }
        if (status == UrbanEventStatus.COMPLETED) {
            throw new BusinessException("EVENT_ALREADY_COMPLETED", "Event is already completed");
        }
        this.status = UrbanEventStatus.COMPLETED;
        this.updatedAt = Instant.now();
    }

    private void ensureMutable() {
        if (status == UrbanEventStatus.CANCELLED) {
            throw new BusinessException("EVENT_CANCELLED", "Cannot modify a cancelled event");
        }
        if (status == UrbanEventStatus.COMPLETED) {
            throw new BusinessException("EVENT_ALREADY_COMPLETED", "Cannot modify a completed event");
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UrbanEventType getType() {
        return type;
    }

    public void setType(UrbanEventType type) {
        this.type = type;
    }

    public UrbanEventStatus getStatus() {
        return status;
    }

    public void setStatus(UrbanEventStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Instant getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(Instant startsAt) {
        this.startsAt = startsAt;
    }

    public Instant getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(Instant endsAt) {
        this.endsAt = endsAt;
    }

    public int getExpectedAttendance() {
        return expectedAttendance;
    }

    public void setExpectedAttendance(int expectedAttendance) {
        this.expectedAttendance = expectedAttendance;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
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
