package com.urbanflow.twin.infrastructure.persistence.repository;

import com.urbanflow.twin.infrastructure.persistence.entity.TwinSimulationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TwinSimulationJpaRepository extends JpaRepository<TwinSimulationEntity, UUID> {

    List<TwinSimulationEntity> findByZoneIdOrderByCreatedAtDesc(String zoneId, Pageable pageable);
}
