package com.urbanflow.incident.infrastructure.persistence;

import com.urbanflow.incident.domain.model.Incident;
import com.urbanflow.incident.domain.model.IncidentStatus;
import com.urbanflow.incident.domain.repository.IncidentRepository;
import com.urbanflow.incident.infrastructure.persistence.mapper.IncidentMapper;
import com.urbanflow.incident.infrastructure.persistence.repository.IncidentJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class IncidentRepositoryImpl implements IncidentRepository {

    private final IncidentJpaRepository jpaRepository;

    public IncidentRepositoryImpl(IncidentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Incident save(Incident incident) {
        return IncidentMapper.toDomain(jpaRepository.save(IncidentMapper.toEntity(incident)));
    }

    @Override
    public Optional<Incident> findById(UUID id) {
        return jpaRepository.findById(id).map(IncidentMapper::toDomain);
    }

    @Override
    public List<Incident> findByZoneId(String zoneId) {
        return jpaRepository.findByZoneIdOrderByCreatedAtDesc(zoneId).stream()
                .map(IncidentMapper::toDomain)
                .toList();
    }

    @Override
    public List<Incident> findByStatus(IncidentStatus status) {
        return jpaRepository.findByStatusOrderByCreatedAtDesc(status).stream()
                .map(IncidentMapper::toDomain)
                .toList();
    }

    @Override
    public List<Incident> findAll(int page, int size) {
        return jpaRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size)).stream()
                .map(IncidentMapper::toDomain)
                .toList();
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}
