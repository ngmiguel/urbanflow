package com.urbanflow.alert.infrastructure.persistence;

import com.urbanflow.alert.domain.model.Alert;
import com.urbanflow.alert.domain.model.AlertStatus;
import com.urbanflow.alert.domain.repository.AlertRepository;
import com.urbanflow.alert.infrastructure.persistence.mapper.AlertMapper;
import com.urbanflow.alert.infrastructure.persistence.repository.AlertJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AlertRepositoryImpl implements AlertRepository {

    private final AlertJpaRepository jpaRepository;

    public AlertRepositoryImpl(AlertJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Alert save(Alert alert) {
        return AlertMapper.toDomain(jpaRepository.save(AlertMapper.toEntity(alert)));
    }

    @Override
    public Optional<Alert> findById(UUID id) {
        return jpaRepository.findById(id).map(AlertMapper::toDomain);
    }

    @Override
    public List<Alert> findByZoneId(String zoneId) {
        return jpaRepository.findByZoneIdOrderByCreatedAtDesc(zoneId).stream()
                .map(AlertMapper::toDomain)
                .toList();
    }

    @Override
    public List<Alert> findByStatus(AlertStatus status) {
        return jpaRepository.findByStatusOrderByCreatedAtDesc(status).stream()
                .map(AlertMapper::toDomain)
                .toList();
    }

    @Override
    public List<Alert> findAll(int page, int size) {
        return jpaRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size)).stream()
                .map(AlertMapper::toDomain)
                .toList();
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}
