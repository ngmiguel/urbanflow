package com.urbanflow.alert.application.service;

import com.urbanflow.alert.application.dto.AlertDto;
import com.urbanflow.alert.domain.model.Alert;
import com.urbanflow.alert.domain.model.AlertStatus;
import com.urbanflow.alert.domain.repository.AlertRepository;
import com.urbanflow.common.dto.PageMeta;
import com.urbanflow.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AlertApplicationService {

    private final AlertRepository alertRepository;

    public AlertApplicationService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Transactional(readOnly = true)
    public AlertDto getAlert(UUID id) {
        return alertRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Alert", id));
    }

    @Transactional(readOnly = true)
    public List<AlertDto> getAlertsByZone(String zoneId) {
        return alertRepository.findByZoneId(zoneId).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<AlertDto> getAlertsByStatus(AlertStatus status) {
        return alertRepository.findByStatus(status).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public PagedAlerts getAllAlerts(int page, int size) {
        List<AlertDto> alerts = alertRepository.findAll(page, size).stream()
                .map(this::toDto)
                .toList();
        long total = alertRepository.count();
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) total / size);
        return new PagedAlerts(alerts, new PageMeta(page, size, total, totalPages));
    }

    @Transactional
    public AlertDto acknowledgeAlert(UUID id) {
        Alert alert = loadAlert(id);
        alert.acknowledge();
        return toDto(alertRepository.save(alert));
    }

    @Transactional
    public AlertDto dismissAlert(UUID id) {
        Alert alert = loadAlert(id);
        alert.dismiss();
        return toDto(alertRepository.save(alert));
    }

    AlertDto toDto(Alert alert) {
        return new AlertDto(
                alert.getId(),
                alert.getRuleId(),
                alert.getAlertType(),
                alert.getSeverity(),
                alert.getMessage(),
                alert.getZoneId(),
                alert.getStatus(),
                alert.getSourceEventId(),
                alert.getCreatedAt(),
                alert.getUpdatedAt()
        );
    }

    private Alert loadAlert(UUID id) {
        return alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert", id));
    }

    public record PagedAlerts(List<AlertDto> content, PageMeta page) {
    }
}
