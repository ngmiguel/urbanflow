package com.urbanflow.alert.infrastructure.persistence;

import com.urbanflow.alert.domain.model.AlertRule;
import com.urbanflow.alert.domain.model.RuleSourceType;
import com.urbanflow.alert.domain.repository.AlertRuleRepository;
import com.urbanflow.alert.infrastructure.persistence.mapper.AlertMapper;
import com.urbanflow.alert.infrastructure.persistence.repository.AlertRuleJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AlertRuleRepositoryImpl implements AlertRuleRepository {

    private final AlertRuleJpaRepository jpaRepository;

    public AlertRuleRepositoryImpl(AlertRuleJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public AlertRule save(AlertRule rule) {
        return AlertMapper.toDomain(jpaRepository.save(AlertMapper.toEntity(rule)));
    }

    @Override
    public Optional<AlertRule> findById(UUID id) {
        return jpaRepository.findById(id).map(AlertMapper::toDomain);
    }

    @Override
    public List<AlertRule> findBySourceTypeAndEnabledTrue(RuleSourceType sourceType) {
        return jpaRepository.findBySourceTypeAndEnabledTrue(sourceType).stream()
                .map(AlertMapper::toDomain)
                .toList();
    }

    @Override
    public List<AlertRule> findAll(int page, int size) {
        return jpaRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size)).stream()
                .map(AlertMapper::toDomain)
                .toList();
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
