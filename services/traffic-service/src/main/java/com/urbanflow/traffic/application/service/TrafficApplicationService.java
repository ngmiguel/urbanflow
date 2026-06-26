package com.urbanflow.traffic.application.service;

import com.urbanflow.common.dto.PageMeta;
import com.urbanflow.common.exception.ResourceNotFoundException;
import com.urbanflow.common.util.IdGenerator;
import com.urbanflow.events.EventMetadata;
import com.urbanflow.events.traffic.TrafficEventType;
import com.urbanflow.events.traffic.TrafficUpdateEvent;
import com.urbanflow.traffic.application.dto.TrafficSegmentDto;
import com.urbanflow.traffic.application.port.TrafficEventPublisher;
import com.urbanflow.traffic.domain.model.TrafficSegment;
import com.urbanflow.traffic.domain.repository.TrafficSegmentRepository;
import com.urbanflow.common.enums.CongestionLevel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TrafficApplicationService {

    private final TrafficSegmentRepository segmentRepository;
    private final TrafficEventPublisher eventPublisher;

    public TrafficApplicationService(
            TrafficSegmentRepository segmentRepository,
            TrafficEventPublisher eventPublisher
    ) {
        this.segmentRepository = segmentRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public TrafficSegmentDto createSegment(
            String name,
            String zoneId,
            CongestionLevel congestionLevel,
            double averageSpeedKmh,
            int vehicleCount,
            double latitude,
            double longitude,
            String correlationId
    ) {
        TrafficSegment segment = TrafficSegment.create(
                name, zoneId, congestionLevel, averageSpeedKmh, vehicleCount, latitude, longitude
        );
        TrafficSegment saved = segmentRepository.save(segment);
        publishEvent(saved, TrafficEventType.SEGMENT_CREATED, correlationId);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public TrafficSegmentDto getSegment(UUID id) {
        return segmentRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("TrafficSegment", id));
    }

    @Transactional(readOnly = true)
    public List<TrafficSegmentDto> getSegmentsByZone(String zoneId) {
        return segmentRepository.findByZoneId(zoneId).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public PagedSegments getAllSegments(int page, int size) {
        List<TrafficSegmentDto> segments = segmentRepository.findAll(page, size).stream()
                .map(this::toDto)
                .toList();
        long total = segmentRepository.count();
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) total / size);
        return new PagedSegments(segments, new PageMeta(page, size, total, totalPages));
    }

    @Transactional
    public TrafficSegmentDto updateSegmentMetrics(
            UUID id,
            CongestionLevel congestionLevel,
            double averageSpeedKmh,
            int vehicleCount,
            String correlationId
    ) {
        TrafficSegment segment = segmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TrafficSegment", id));

        CongestionLevel previousLevel = segment.getCongestionLevel();
        segment.updateMetrics(congestionLevel, averageSpeedKmh, vehicleCount);
        TrafficSegment saved = segmentRepository.save(segment);

        TrafficEventType eventType = previousLevel == congestionLevel
                ? TrafficEventType.SEGMENT_UPDATED
                : TrafficEventType.CONGESTION_CHANGED;
        publishEvent(saved, eventType, correlationId);
        return toDto(saved);
    }

    private void publishEvent(TrafficSegment segment, TrafficEventType eventType, String correlationId) {
        String corrId = correlationId != null && !correlationId.isBlank()
                ? correlationId
                : IdGenerator.newCorrelationId();

        TrafficUpdateEvent event = new TrafficUpdateEvent(
                EventMetadata.create(eventType.name(), "traffic-service", corrId),
                segment.getId(),
                segment.getName(),
                eventType,
                segment.getCongestionLevel(),
                segment.getAverageSpeedKmh(),
                segment.getVehicleCount(),
                segment.getZoneId()
        );
        eventPublisher.publish(event);
    }

    private TrafficSegmentDto toDto(TrafficSegment segment) {
        return new TrafficSegmentDto(
                segment.getId(),
                segment.getName(),
                segment.getZoneId(),
                segment.getCongestionLevel(),
                segment.getAverageSpeedKmh(),
                segment.getVehicleCount(),
                segment.getLatitude(),
                segment.getLongitude(),
                segment.getCreatedAt(),
                segment.getUpdatedAt()
        );
    }

    public record PagedSegments(List<TrafficSegmentDto> content, PageMeta page) {
    }
}
