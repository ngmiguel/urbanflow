package com.urbanflow.analytics.domain.repository;

import com.urbanflow.analytics.domain.model.MetricHistoryEntry;
import com.urbanflow.analytics.domain.model.MetricType;

import java.util.List;

public interface MetricHistoryRepository {

    MetricHistoryEntry save(MetricHistoryEntry entry);

    List<MetricHistoryEntry> findByZoneAndType(String zoneId, MetricType metricType, int limit);
}
