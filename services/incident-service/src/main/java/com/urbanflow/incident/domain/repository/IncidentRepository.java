package com.urbanflow.incident.domain.repository;

import com.urbanflow.incident.domain.model.Incident;
import com.urbanflow.incident.domain.model.IncidentStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IncidentRepository {

    Incident save(Incident incident);

    Optional<Incident> findById(UUID id);

    List<Incident> findByZoneId(String zoneId);

    List<Incident> findByStatus(IncidentStatus status);

    List<Incident> findAll(int page, int size);

    long count();
}
