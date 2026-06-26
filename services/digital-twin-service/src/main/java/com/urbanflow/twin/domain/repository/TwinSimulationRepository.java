package com.urbanflow.twin.domain.repository;

import com.urbanflow.twin.domain.model.TwinSimulation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TwinSimulationRepository {

    TwinSimulation save(TwinSimulation simulation);

    Optional<TwinSimulation> findById(UUID id);

    List<TwinSimulation> findByZoneId(String zoneId, int limit);
}
