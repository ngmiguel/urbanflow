package com.urbanflow.alert.infrastructure.persistence.repository;

import com.urbanflow.alert.domain.model.RuleSourceType;
import com.urbanflow.alert.infrastructure.persistence.entity.AlertRuleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AlertRuleJpaRepository extends JpaRepository<AlertRuleEntity, UUID> {

    List<AlertRuleEntity> findBySourceTypeAndEnabledTrue(RuleSourceType sourceType);

    Page<AlertRuleEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
