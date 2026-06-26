package com.urbanflow.environment.infrastructure.persistence;

import com.urbanflow.environment.domain.model.SensorReading;
import com.urbanflow.environment.domain.repository.SensorReadingRepository;
import com.urbanflow.environment.infrastructure.persistence.mapper.SensorReadingMapper;
import com.urbanflow.environment.infrastructure.persistence.repository.SensorReadingJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SensorReadingRepositoryImpl implements SensorReadingRepository {

    private final SensorReadingJpaRepository jpaRepository;

    public SensorReadingRepositoryImpl(SensorReadingJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public SensorReading save(SensorReading reading) {
        return SensorReadingMapper.toDomain(jpaRepository.save(SensorReadingMapper.toEntity(reading)));
    }

    @Override
    public Optional<SensorReading> findById(UUID id) {
        return jpaRepository.findById(id).map(SensorReadingMapper::toDomain);
    }

    @Override
    public List<SensorReading> findByZoneId(String zoneId, int page, int size) {
        return jpaRepository.findByZoneIdOrderByRecordedAtDesc(zoneId, PageRequest.of(page, size)).stream()
                .map(SensorReadingMapper::toDomain)
                .toList();
    }

    @Override
    public List<SensorReading> findByZoneIdSince(String zoneId, Instant since) {
        return jpaRepository.findByZoneIdAndRecordedAtAfterOrderByRecordedAtDesc(zoneId, since).stream()
                .map(SensorReadingMapper::toDomain)
                .toList();
    }

    @Override
    public List<SensorReading> findAll(int page, int size) {
        return jpaRepository.findAllByOrderByRecordedAtDesc(PageRequest.of(page, size)).stream()
                .map(SensorReadingMapper::toDomain)
                .toList();
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}
