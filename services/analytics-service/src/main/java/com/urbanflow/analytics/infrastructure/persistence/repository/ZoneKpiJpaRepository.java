package com.urbanflow.analytics.infrastructure.persistence.repository;

import com.urbanflow.analytics.infrastructure.persistence.entity.ZoneKpiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneKpiJpaRepository extends JpaRepository<ZoneKpiEntity, String> {
}
