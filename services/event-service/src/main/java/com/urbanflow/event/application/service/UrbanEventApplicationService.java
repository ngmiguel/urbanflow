package com.urbanflow.event.application.service;

import com.urbanflow.common.dto.PageMeta;
import com.urbanflow.common.exception.ResourceNotFoundException;
import com.urbanflow.common.util.IdGenerator;
import com.urbanflow.event.application.dto.UrbanEventDto;
import com.urbanflow.event.application.port.UrbanEventPublisher;
import com.urbanflow.event.domain.model.UrbanEvent;
import com.urbanflow.event.domain.model.UrbanEventStatus;
import com.urbanflow.event.domain.model.UrbanEventType;
import com.urbanflow.event.domain.repository.UrbanEventRepository;
import com.urbanflow.events.EventMetadata;
import com.urbanflow.events.urban.UrbanPlannedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class UrbanEventApplicationService {

    private final UrbanEventRepository urbanEventRepository;
    private final UrbanEventPublisher urbanEventPublisher;

    public UrbanEventApplicationService(
            UrbanEventRepository urbanEventRepository,
            UrbanEventPublisher urbanEventPublisher
    ) {
        this.urbanEventRepository = urbanEventRepository;
        this.urbanEventPublisher = urbanEventPublisher;
    }

    @Transactional
    public UrbanEventDto scheduleEvent(
            UrbanEventType type,
            String title,
            String description,
            String zoneId,
            double latitude,
            double longitude,
            Instant startsAt,
            Instant endsAt,
            int expectedAttendance,
            String organizer,
            String correlationId
    ) {
        UrbanEvent saved = urbanEventRepository.save(UrbanEvent.schedule(
                type,
                title,
                description,
                zoneId,
                latitude,
                longitude,
                startsAt,
                endsAt,
                expectedAttendance,
                organizer
        ));
        publishEvent(saved, UrbanPlannedEvent.UrbanEventLifecycleType.SCHEDULED, correlationId);
        return UrbanEventDto.from(saved);
    }

    @Transactional(readOnly = true)
    public UrbanEventDto getEvent(UUID id) {
        return urbanEventRepository.findById(id)
                .map(UrbanEventDto::from)
                .orElseThrow(() -> new ResourceNotFoundException("UrbanEvent", id));
    }

    @Transactional(readOnly = true)
    public PagedEvents getAllEvents(int page, int size) {
        List<UrbanEventDto> events = urbanEventRepository.findAll(page, size).stream()
                .map(UrbanEventDto::from)
                .toList();
        long total = urbanEventRepository.count();
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) total / size);
        return new PagedEvents(events, new PageMeta(page, size, total, totalPages));
    }

    @Transactional(readOnly = true)
    public List<UrbanEventDto> getEventsByZone(String zoneId) {
        return urbanEventRepository.findByZoneId(zoneId).stream().map(UrbanEventDto::from).toList();
    }

    @Transactional(readOnly = true)
    public List<UrbanEventDto> getUpcomingEvents(String zoneId, int limit) {
        return urbanEventRepository.findUpcomingByZoneId(zoneId, Instant.now(), limit).stream()
                .map(UrbanEventDto::from)
                .toList();
    }

    @Transactional
    public UrbanEventDto updateEvent(
            UUID id,
            String title,
            String description,
            Instant startsAt,
            Instant endsAt,
            int expectedAttendance,
            String correlationId
    ) {
        UrbanEvent event = loadEvent(id);
        event.update(title, description, startsAt, endsAt, expectedAttendance);
        UrbanEvent saved = urbanEventRepository.save(event);
        publishEvent(saved, UrbanPlannedEvent.UrbanEventLifecycleType.UPDATED, correlationId);
        return UrbanEventDto.from(saved);
    }

    @Transactional
    public UrbanEventDto cancelEvent(UUID id, String correlationId) {
        UrbanEvent event = loadEvent(id);
        event.cancel();
        UrbanEvent saved = urbanEventRepository.save(event);
        publishEvent(saved, UrbanPlannedEvent.UrbanEventLifecycleType.CANCELLED, correlationId);
        return UrbanEventDto.from(saved);
    }

    @Transactional
    public UrbanEventDto completeEvent(UUID id, String correlationId) {
        UrbanEvent event = loadEvent(id);
        event.complete();
        UrbanEvent saved = urbanEventRepository.save(event);
        publishEvent(saved, UrbanPlannedEvent.UrbanEventLifecycleType.COMPLETED, correlationId);
        return UrbanEventDto.from(saved);
    }

    private UrbanEvent loadEvent(UUID id) {
        return urbanEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UrbanEvent", id));
    }

    private void publishEvent(
            UrbanEvent event,
            UrbanPlannedEvent.UrbanEventLifecycleType lifecycleType,
            String correlationId
    ) {
        String corrId = correlationId != null && !correlationId.isBlank()
                ? correlationId
                : IdGenerator.newCorrelationId();

        UrbanPlannedEvent plannedEvent = new UrbanPlannedEvent(
                EventMetadata.create(lifecycleType.name(), "event-service", corrId),
                event.getId(),
                lifecycleType,
                event.getType().name(),
                event.getTitle(),
                event.getDescription(),
                event.getZoneId(),
                event.getLatitude(),
                event.getLongitude(),
                event.getStartsAt(),
                event.getEndsAt(),
                event.getExpectedAttendance()
        );
        urbanEventPublisher.publish(plannedEvent);
    }

    public record PagedEvents(List<UrbanEventDto> content, PageMeta page) {
    }
}
