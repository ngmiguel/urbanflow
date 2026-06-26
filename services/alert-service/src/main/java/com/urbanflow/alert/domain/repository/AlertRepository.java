package com.urbanflow.alert.domain.repository;

import com.urbanflow.alert.domain.model.Alert;
import com.urbanflow.alert.domain.model.AlertStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlertRepository {

    Alert save(Alert alert);

    Optional<Alert> findById(UUID id);

    List<Alert> findByZoneId(String zoneId);

    List<Alert> findByStatus(AlertStatus status);

    List<Alert> findAll(int page, int size);

    long count();
}
