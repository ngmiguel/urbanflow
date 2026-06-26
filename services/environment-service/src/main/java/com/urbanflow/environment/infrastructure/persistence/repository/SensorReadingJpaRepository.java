package com.urbanflow.environment.infrastructure.persistence.repository;

import com.urbanflow.environment.infrastructure.persistence.entity.SensorReadingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface SensorReadingJpaRepository extends JpaRepository<SensorReadingEntity, UUID> {

    Page<SensorReadingEntity> findByZoneIdOrderByRecordedAtDesc(String zoneId, Pageable pageable);

    List<SensorReadingEntity> findByZoneIdAndRecordedAtAfterOrderByRecordedAtDesc(String zoneId, Instant since);

    Page<SensorReadingEntity> findAllByOrderByRecordedAtDesc(Pageable pageable);
}
