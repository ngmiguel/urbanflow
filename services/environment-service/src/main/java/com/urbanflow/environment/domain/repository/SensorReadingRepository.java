package com.urbanflow.environment.domain.repository;

import com.urbanflow.environment.domain.model.SensorReading;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SensorReadingRepository {

    SensorReading save(SensorReading reading);

    Optional<SensorReading> findById(UUID id);

    List<SensorReading> findByZoneId(String zoneId, int page, int size);

    List<SensorReading> findByZoneIdSince(String zoneId, Instant since);

    List<SensorReading> findAll(int page, int size);

    long count();
}
