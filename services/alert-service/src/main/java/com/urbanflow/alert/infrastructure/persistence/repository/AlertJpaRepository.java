package com.urbanflow.alert.infrastructure.persistence.repository;

import com.urbanflow.alert.domain.model.AlertStatus;
import com.urbanflow.alert.infrastructure.persistence.entity.AlertEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AlertJpaRepository extends JpaRepository<AlertEntity, UUID> {

    List<AlertEntity> findByZoneIdOrderByCreatedAtDesc(String zoneId);

    List<AlertEntity> findByStatusOrderByCreatedAtDesc(AlertStatus status);

    Page<AlertEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
