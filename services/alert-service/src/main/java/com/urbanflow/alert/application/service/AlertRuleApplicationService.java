package com.urbanflow.alert.application.service;

import com.urbanflow.alert.application.dto.AlertRuleDto;
import com.urbanflow.alert.domain.model.AlertRule;
import com.urbanflow.alert.domain.model.AlertSeverity;
import com.urbanflow.alert.domain.model.RuleSourceType;
import com.urbanflow.alert.domain.repository.AlertRuleRepository;
import com.urbanflow.common.dto.PageMeta;
import com.urbanflow.common.exception.ResourceNotFoundException;
import com.urbanflow.common.enums.CongestionLevel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AlertRuleApplicationService {

    private final AlertRuleRepository alertRuleRepository;

    public AlertRuleApplicationService(AlertRuleRepository alertRuleRepository) {
        this.alertRuleRepository = alertRuleRepository;
    }

    @Transactional
    public AlertRuleDto createRule(
            String name,
            RuleSourceType sourceType,
            String zoneId,
            AlertSeverity outputSeverity,
            AlertSeverity incidentMinSeverity,
            CongestionLevel minCongestionLevel,
            String sensorType,
            Double sensorThreshold
    ) {
        AlertRule saved = alertRuleRepository.save(AlertRule.create(
                name,
                sourceType,
                zoneId,
                outputSeverity,
                incidentMinSeverity,
                minCongestionLevel,
                sensorType,
                sensorThreshold
        ));
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public AlertRuleDto getRule(UUID id) {
        return alertRuleRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("AlertRule", id));
    }

    @Transactional(readOnly = true)
    public PagedRules getAllRules(int page, int size) {
        List<AlertRuleDto> rules = alertRuleRepository.findAll(page, size).stream()
                .map(this::toDto)
                .toList();
        long total = alertRuleRepository.count();
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) total / size);
        return new PagedRules(rules, new PageMeta(page, size, total, totalPages));
    }

    @Transactional
    public AlertRuleDto updateRule(
            UUID id,
            String name,
            String zoneId,
            AlertSeverity outputSeverity,
            boolean enabled,
            AlertSeverity incidentMinSeverity,
            CongestionLevel minCongestionLevel,
            String sensorType,
            Double sensorThreshold
    ) {
        AlertRule rule = alertRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AlertRule", id));
        rule.update(name, zoneId, outputSeverity, enabled, incidentMinSeverity,
                minCongestionLevel, sensorType, sensorThreshold);
        return toDto(alertRuleRepository.save(rule));
    }

    @Transactional
    public void deleteRule(UUID id) {
        if (alertRuleRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("AlertRule", id);
        }
        alertRuleRepository.deleteById(id);
    }

    private AlertRuleDto toDto(AlertRule rule) {
        return new AlertRuleDto(
                rule.getId(),
                rule.getName(),
                rule.getSourceType(),
                rule.getZoneId(),
                rule.getOutputSeverity(),
                rule.isEnabled(),
                rule.getIncidentMinSeverity(),
                rule.getMinCongestionLevel(),
                rule.getSensorType(),
                rule.getSensorThreshold(),
                rule.getCreatedAt(),
                rule.getUpdatedAt()
        );
    }

    public record PagedRules(List<AlertRuleDto> content, PageMeta page) {
    }
}
