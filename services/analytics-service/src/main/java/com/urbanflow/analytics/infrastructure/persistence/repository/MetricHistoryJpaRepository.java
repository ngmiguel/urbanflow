package com.urbanflow.analytics.infrastructure.persistence.repository;

import com.urbanflow.analytics.domain.model.MetricType;
import com.urbanflow.analytics.infrastructure.persistence.entity.MetricHistoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MetricHistoryJpaRepository extends JpaRepository<MetricHistoryEntity, UUID> {

    List<MetricHistoryEntity> findByZoneIdAndMetricTypeOrderByRecordedAtDesc(
            String zoneId,
            MetricType metricType,
            Pageable pageable
    );
}
