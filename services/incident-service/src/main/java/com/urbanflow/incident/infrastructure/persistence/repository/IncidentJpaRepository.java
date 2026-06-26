package com.urbanflow.incident.infrastructure.persistence.repository;

import com.urbanflow.incident.domain.model.IncidentStatus;
import com.urbanflow.incident.infrastructure.persistence.entity.IncidentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IncidentJpaRepository extends JpaRepository<IncidentEntity, UUID> {

    List<IncidentEntity> findByZoneIdOrderByCreatedAtDesc(String zoneId);

    List<IncidentEntity> findByStatusOrderByCreatedAtDesc(IncidentStatus status);

    Page<IncidentEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
