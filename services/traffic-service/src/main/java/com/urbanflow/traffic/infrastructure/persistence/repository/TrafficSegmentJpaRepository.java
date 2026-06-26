package com.urbanflow.traffic.infrastructure.persistence.repository;

import com.urbanflow.traffic.infrastructure.persistence.entity.TrafficSegmentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TrafficSegmentJpaRepository extends JpaRepository<TrafficSegmentEntity, UUID> {

    List<TrafficSegmentEntity> findByZoneIdOrderByNameAsc(String zoneId);

    List<TrafficSegmentEntity> findAllBy(Pageable pageable);
}
