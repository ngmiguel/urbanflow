package com.urbanflow.analytics.application.service;

import com.urbanflow.analytics.domain.model.MetricHistoryEntry;
import com.urbanflow.analytics.domain.model.MetricType;
import com.urbanflow.analytics.domain.model.ZoneKpi;
import com.urbanflow.analytics.domain.repository.MetricHistoryRepository;
import com.urbanflow.analytics.domain.repository.ZoneKpiRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsQueryService {

    private final ZoneKpiRepository zoneKpiRepository;
    private final MetricHistoryRepository metricHistoryRepository;

    public AnalyticsQueryService(
            ZoneKpiRepository zoneKpiRepository,
            MetricHistoryRepository metricHistoryRepository
    ) {
        this.zoneKpiRepository = zoneKpiRepository;
        this.metricHistoryRepository = metricHistoryRepository;
    }

    public ZoneKpi getZoneKpis(String zoneId) {
        return zoneKpiRepository.findByZoneId(zoneId)
                .orElseGet(() -> new ZoneKpi(zoneId));
    }

    public List<ZoneKpi> getDashboard() {
        return zoneKpiRepository.findAll();
    }

    public List<MetricHistoryEntry> getMetricHistory(String zoneId, MetricType metricType, int limit) {
        return metricHistoryRepository.findByZoneAndType(zoneId, metricType, limit);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardSummary() {
        List<ZoneKpi> zones = zoneKpiRepository.findAll();
        long totalAlerts = zones.stream().mapToLong(ZoneKpi::getAlertCount).sum();
        long totalIncidents = zones.stream().mapToLong(ZoneKpi::getIncidentCount).sum();
        long totalAnomalies = zones.stream().mapToLong(ZoneKpi::getAnomalyCount).sum();
        double avgSpeed = zones.stream()
                .filter(zone -> zone.getTrafficUpdateCount() > 0)
                .mapToDouble(ZoneKpi::getAverageSpeedKmh)
                .average()
                .orElse(0.0);

        Map<String, Object> summary = new HashMap<>();
        summary.put("zoneCount", zones.size());
        summary.put("totalAlerts", totalAlerts);
        summary.put("totalIncidents", totalIncidents);
        summary.put("totalAnomalies", totalAnomalies);
        summary.put("networkAverageSpeedKmh", avgSpeed);
        return summary;
    }
}
