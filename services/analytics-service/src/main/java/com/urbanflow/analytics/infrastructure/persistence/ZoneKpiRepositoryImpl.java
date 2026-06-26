package com.urbanflow.analytics.infrastructure.persistence;

import com.urbanflow.analytics.domain.model.ZoneKpi;
import com.urbanflow.analytics.domain.repository.ZoneKpiRepository;
import com.urbanflow.analytics.infrastructure.persistence.mapper.AnalyticsMapper;
import com.urbanflow.analytics.infrastructure.persistence.repository.ZoneKpiJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ZoneKpiRepositoryImpl implements ZoneKpiRepository {

    private final ZoneKpiJpaRepository jpaRepository;

    public ZoneKpiRepositoryImpl(ZoneKpiJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ZoneKpi save(ZoneKpi zoneKpi) {
        return AnalyticsMapper.toDomain(jpaRepository.save(AnalyticsMapper.toEntity(zoneKpi)));
    }

    @Override
    public Optional<ZoneKpi> findByZoneId(String zoneId) {
        return jpaRepository.findById(zoneId).map(AnalyticsMapper::toDomain);
    }

    @Override
    public List<ZoneKpi> findAll() {
        return jpaRepository.findAll().stream().map(AnalyticsMapper::toDomain).toList();
    }
}
