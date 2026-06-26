package com.urbanflow.twin.infrastructure.persistence;

import com.urbanflow.twin.domain.model.TwinSimulation;
import com.urbanflow.twin.domain.repository.TwinSimulationRepository;
import com.urbanflow.twin.infrastructure.persistence.mapper.TwinSimulationMapper;
import com.urbanflow.twin.infrastructure.persistence.repository.TwinSimulationJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TwinSimulationRepositoryImpl implements TwinSimulationRepository {

    private final TwinSimulationJpaRepository jpaRepository;

    public TwinSimulationRepositoryImpl(TwinSimulationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public TwinSimulation save(TwinSimulation simulation) {
        return TwinSimulationMapper.toDomain(
                jpaRepository.save(TwinSimulationMapper.toEntity(simulation))
        );
    }

    @Override
    public Optional<TwinSimulation> findById(UUID id) {
        return jpaRepository.findById(id).map(TwinSimulationMapper::toDomain);
    }

    @Override
    public List<TwinSimulation> findByZoneId(String zoneId, int limit) {
        return jpaRepository.findByZoneIdOrderByCreatedAtDesc(zoneId, PageRequest.of(0, limit)).stream()
                .map(TwinSimulationMapper::toDomain)
                .toList();
    }
}
