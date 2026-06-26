package com.urbanflow.analytics.infrastructure.persistence;

import com.urbanflow.analytics.domain.model.MetricHistoryEntry;
import com.urbanflow.analytics.domain.model.MetricType;
import com.urbanflow.analytics.domain.repository.MetricHistoryRepository;
import com.urbanflow.analytics.infrastructure.persistence.mapper.AnalyticsMapper;
import com.urbanflow.analytics.infrastructure.persistence.repository.MetricHistoryJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MetricHistoryRepositoryImpl implements MetricHistoryRepository {

    private final MetricHistoryJpaRepository jpaRepository;

    public MetricHistoryRepositoryImpl(MetricHistoryJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public MetricHistoryEntry save(MetricHistoryEntry entry) {
        return AnalyticsMapper.toDomain(jpaRepository.save(AnalyticsMapper.toEntity(entry)));
    }

    @Override
    public List<MetricHistoryEntry> findByZoneAndType(String zoneId, MetricType metricType, int limit) {
        return jpaRepository.findByZoneIdAndMetricTypeOrderByRecordedAtDesc(
                zoneId,
                metricType,
                PageRequest.of(0, limit)
        ).stream().map(AnalyticsMapper::toDomain).toList();
    }
}
