package com.urbanflow.traffic.infrastructure.persistence;

import com.urbanflow.traffic.domain.model.TrafficSegment;
import com.urbanflow.traffic.domain.repository.TrafficSegmentRepository;
import com.urbanflow.traffic.infrastructure.persistence.mapper.TrafficSegmentMapper;
import com.urbanflow.traffic.infrastructure.persistence.repository.TrafficSegmentJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TrafficSegmentRepositoryImpl implements TrafficSegmentRepository {

    private final TrafficSegmentJpaRepository jpaRepository;

    public TrafficSegmentRepositoryImpl(TrafficSegmentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public TrafficSegment save(TrafficSegment segment) {
        return TrafficSegmentMapper.toDomain(
                jpaRepository.save(TrafficSegmentMapper.toEntity(segment))
        );
    }

    @Override
    public Optional<TrafficSegment> findById(UUID id) {
        return jpaRepository.findById(id).map(TrafficSegmentMapper::toDomain);
    }

    @Override
    public List<TrafficSegment> findByZoneId(String zoneId) {
        return jpaRepository.findByZoneIdOrderByNameAsc(zoneId).stream()
                .map(TrafficSegmentMapper::toDomain)
                .toList();
    }

    @Override
    public List<TrafficSegment> findAll(int page, int size) {
        return jpaRepository.findAllBy(PageRequest.of(page, size)).stream()
                .map(TrafficSegmentMapper::toDomain)
                .toList();
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}
