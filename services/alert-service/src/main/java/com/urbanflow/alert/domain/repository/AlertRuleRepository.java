package com.urbanflow.alert.domain.repository;

import com.urbanflow.alert.domain.model.AlertRule;
import com.urbanflow.alert.domain.model.RuleSourceType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlertRuleRepository {

    AlertRule save(AlertRule rule);

    Optional<AlertRule> findById(UUID id);

    List<AlertRule> findBySourceTypeAndEnabledTrue(RuleSourceType sourceType);

    List<AlertRule> findAll(int page, int size);

    long count();

    void deleteById(UUID id);
}
