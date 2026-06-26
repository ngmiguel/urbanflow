package com.urbanflow.traffic.infrastructure.persistence.mapper;

import com.urbanflow.traffic.domain.model.TrafficSegment;
import com.urbanflow.traffic.infrastructure.persistence.entity.TrafficSegmentEntity;

public final class TrafficSegmentMapper {

    private TrafficSegmentMapper() {
    }

    public static TrafficSegmentEntity toEntity(TrafficSegment segment) {
        TrafficSegmentEntity entity = new TrafficSegmentEntity();
        entity.setId(segment.getId());
        entity.setName(segment.getName());
        entity.setZoneId(segment.getZoneId());
        entity.setCongestionLevel(segment.getCongestionLevel());
        entity.setAverageSpeedKmh(segment.getAverageSpeedKmh());
        entity.setVehicleCount(segment.getVehicleCount());
        entity.setLatitude(segment.getLatitude());
        entity.setLongitude(segment.getLongitude());
        entity.setCreatedAt(segment.getCreatedAt());
        entity.setUpdatedAt(segment.getUpdatedAt());
        return entity;
    }

    public static TrafficSegment toDomain(TrafficSegmentEntity entity) {
        return new TrafficSegment(
                entity.getId(),
                entity.getName(),
                entity.getZoneId(),
                entity.getCongestionLevel(),
                entity.getAverageSpeedKmh(),
                entity.getVehicleCount(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
