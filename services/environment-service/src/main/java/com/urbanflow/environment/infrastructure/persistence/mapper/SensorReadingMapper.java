package com.urbanflow.environment.infrastructure.persistence.mapper;

import com.urbanflow.environment.domain.model.SensorReading;
import com.urbanflow.environment.infrastructure.persistence.entity.SensorReadingEntity;

public final class SensorReadingMapper {

    private SensorReadingMapper() {
    }

    public static SensorReading toDomain(SensorReadingEntity entity) {
        return new SensorReading(
                entity.getId(),
                entity.getDeviceId(),
                entity.getSensorType(),
                entity.getValue(),
                entity.getUnit(),
                entity.getZoneId(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getSourceEventId(),
                entity.getRecordedAt()
        );
    }

    public static SensorReadingEntity toEntity(SensorReading reading) {
        SensorReadingEntity entity = new SensorReadingEntity();
        entity.setId(reading.getId());
        entity.setDeviceId(reading.getDeviceId());
        entity.setSensorType(reading.getSensorType());
        entity.setValue(reading.getValue());
        entity.setUnit(reading.getUnit());
        entity.setZoneId(reading.getZoneId());
        entity.setLatitude(reading.getLatitude());
        entity.setLongitude(reading.getLongitude());
        entity.setSourceEventId(reading.getSourceEventId());
        entity.setRecordedAt(reading.getRecordedAt());
        return entity;
    }
}
