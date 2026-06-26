package com.urbanflow.alert.infrastructure.persistence.mapper;

import com.urbanflow.alert.domain.model.Alert;
import com.urbanflow.alert.domain.model.AlertRule;
import com.urbanflow.alert.infrastructure.persistence.entity.AlertEntity;
import com.urbanflow.alert.infrastructure.persistence.entity.AlertRuleEntity;

public final class AlertMapper {

    private AlertMapper() {
    }

    public static AlertRule toDomain(AlertRuleEntity entity) {
        return new AlertRule(
                entity.getId(),
                entity.getName(),
                entity.getSourceType(),
                entity.getZoneId(),
                entity.getOutputSeverity(),
                entity.isEnabled(),
                entity.getIncidentMinSeverity(),
                entity.getMinCongestionLevel(),
                entity.getSensorType(),
                entity.getSensorThreshold(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static AlertRuleEntity toEntity(AlertRule rule) {
        AlertRuleEntity entity = new AlertRuleEntity();
        entity.setId(rule.getId());
        entity.setName(rule.getName());
        entity.setSourceType(rule.getSourceType());
        entity.setZoneId(rule.getZoneId());
        entity.setOutputSeverity(rule.getOutputSeverity());
        entity.setEnabled(rule.isEnabled());
        entity.setIncidentMinSeverity(rule.getIncidentMinSeverity());
        entity.setMinCongestionLevel(rule.getMinCongestionLevel());
        entity.setSensorType(rule.getSensorType());
        entity.setSensorThreshold(rule.getSensorThreshold());
        entity.setCreatedAt(rule.getCreatedAt());
        entity.setUpdatedAt(rule.getUpdatedAt());
        return entity;
    }

    public static Alert toDomain(AlertEntity entity) {
        return new Alert(
                entity.getId(),
                entity.getRuleId(),
                entity.getAlertType(),
                entity.getSeverity(),
                entity.getMessage(),
                entity.getZoneId(),
                entity.getStatus(),
                entity.getSourceEventId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static AlertEntity toEntity(Alert alert) {
        AlertEntity entity = new AlertEntity();
        entity.setId(alert.getId());
        entity.setRuleId(alert.getRuleId());
        entity.setAlertType(alert.getAlertType());
        entity.setSeverity(alert.getSeverity());
        entity.setMessage(alert.getMessage());
        entity.setZoneId(alert.getZoneId());
        entity.setStatus(alert.getStatus());
        entity.setSourceEventId(alert.getSourceEventId());
        entity.setCreatedAt(alert.getCreatedAt());
        entity.setUpdatedAt(alert.getUpdatedAt());
        return entity;
    }
}
