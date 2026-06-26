package com.urbanflow.incident.application.service;

import com.urbanflow.common.dto.PageMeta;
import com.urbanflow.common.exception.ResourceNotFoundException;
import com.urbanflow.common.util.IdGenerator;
import com.urbanflow.events.EventMetadata;
import com.urbanflow.events.incident.IncidentEvent;
import com.urbanflow.incident.application.dto.IncidentDto;
import com.urbanflow.incident.application.port.IncidentEventPublisher;
import com.urbanflow.incident.domain.model.Incident;
import com.urbanflow.incident.domain.model.IncidentSeverity;
import com.urbanflow.incident.domain.model.IncidentStatus;
import com.urbanflow.incident.domain.model.IncidentType;
import com.urbanflow.incident.domain.repository.IncidentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class IncidentApplicationService {

    private final IncidentRepository incidentRepository;
    private final IncidentEventPublisher incidentEventPublisher;

    public IncidentApplicationService(
            IncidentRepository incidentRepository,
            IncidentEventPublisher incidentEventPublisher
    ) {
        this.incidentRepository = incidentRepository;
        this.incidentEventPublisher = incidentEventPublisher;
    }

    @Transactional
    public IncidentDto reportIncident(
            IncidentType type,
            IncidentSeverity severity,
            String description,
            String zoneId,
            double latitude,
            double longitude,
            String reportedBy,
            String correlationId
    ) {
        Incident saved = incidentRepository.save(
                Incident.report(type, severity, description, zoneId, latitude, longitude, reportedBy)
        );
        publishEvent(saved, IncidentEvent.IncidentEventType.REPORTED, correlationId);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public IncidentDto getIncident(UUID id) {
        return incidentRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Incident", id));
    }

    @Transactional(readOnly = true)
    public List<IncidentDto> getIncidentsByZone(String zoneId) {
        return incidentRepository.findByZoneId(zoneId).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<IncidentDto> getIncidentsByStatus(IncidentStatus status) {
        return incidentRepository.findByStatus(status).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public PagedIncidents getAllIncidents(int page, int size) {
        List<IncidentDto> incidents = incidentRepository.findAll(page, size).stream()
                .map(this::toDto)
                .toList();
        long total = incidentRepository.count();
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) total / size);
        return new PagedIncidents(incidents, new PageMeta(page, size, total, totalPages));
    }

    @Transactional
    public IncidentDto updateIncident(
            UUID id,
            IncidentSeverity severity,
            String description,
            IncidentStatus status,
            String correlationId
    ) {
        Incident incident = loadIncident(id);
        incident.update(severity, description, status);
        Incident saved = incidentRepository.save(incident);
        publishEvent(saved, IncidentEvent.IncidentEventType.UPDATED, correlationId);
        return toDto(saved);
    }

    @Transactional
    public IncidentDto resolveIncident(UUID id, String correlationId) {
        Incident incident = loadIncident(id);
        incident.resolve();
        Incident saved = incidentRepository.save(incident);
        publishEvent(saved, IncidentEvent.IncidentEventType.RESOLVED, correlationId);
        return toDto(saved);
    }

    private Incident loadIncident(UUID id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident", id));
    }

    private void publishEvent(
            Incident incident,
            IncidentEvent.IncidentEventType eventType,
            String correlationId
    ) {
        String corrId = correlationId != null && !correlationId.isBlank()
                ? correlationId
                : IdGenerator.newCorrelationId();

        IncidentEvent event = new IncidentEvent(
                EventMetadata.create(eventType.name(), "incident-service", corrId),
                incident.getId(),
                eventType,
                incident.getSeverity().name(),
                incident.getDescription(),
                incident.getLatitude(),
                incident.getLongitude(),
                incident.getZoneId()
        );
        incidentEventPublisher.publish(event);
    }

    private IncidentDto toDto(Incident incident) {
        return new IncidentDto(
                incident.getId(),
                incident.getType(),
                incident.getSeverity(),
                incident.getStatus(),
                incident.getDescription(),
                incident.getZoneId(),
                incident.getLatitude(),
                incident.getLongitude(),
                incident.getReportedBy(),
                incident.getResolvedAt(),
                incident.getCreatedAt(),
                incident.getUpdatedAt()
        );
    }

    public record PagedIncidents(List<IncidentDto> content, PageMeta page) {
    }
}
