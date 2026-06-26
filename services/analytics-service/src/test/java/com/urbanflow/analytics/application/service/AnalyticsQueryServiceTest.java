package com.urbanflow.analytics.application.service;

import com.urbanflow.analytics.domain.model.ZoneKpi;
import com.urbanflow.analytics.domain.repository.MetricHistoryRepository;
import com.urbanflow.analytics.domain.repository.ZoneKpiRepository;
import com.urbanflow.common.enums.CongestionLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsQueryServiceTest {

    @Mock
    private ZoneKpiRepository zoneKpiRepository;

    @Mock
    private MetricHistoryRepository metricHistoryRepository;

    @InjectMocks
    private AnalyticsQueryService analyticsQueryService;

    @Test
    void shouldReturnBaselineKpisWhenZoneMissing() {
        when(zoneKpiRepository.findByZoneId("zone-casa-centre")).thenReturn(Optional.empty());

        ZoneKpi kpi = analyticsQueryService.getZoneKpis("zone-casa-centre");

        assertEquals("zone-casa-centre", kpi.getZoneId());
        assertEquals(CongestionLevel.FREE_FLOW, kpi.getCongestionLevel());
    }

    @Test
    void shouldAggregateDashboardSummary() {
        ZoneKpi zoneA = new ZoneKpi("zone-a");
        zoneA.setAlertCount(2);
        zoneA.setIncidentCount(1);
        zoneA.setAnomalyCount(3);
        zoneA.setTrafficUpdateCount(5);
        zoneA.setAverageSpeedKmh(40.0);

        ZoneKpi zoneB = new ZoneKpi("zone-b");
        zoneB.setAlertCount(1);
        zoneB.setIncidentCount(2);
        zoneB.setAnomalyCount(0);
        zoneB.setTrafficUpdateCount(3);
        zoneB.setAverageSpeedKmh(20.0);

        when(zoneKpiRepository.findAll()).thenReturn(List.of(zoneA, zoneB));

        Map<String, Object> summary = analyticsQueryService.getDashboardSummary();

        assertEquals(2, summary.get("zoneCount"));
        assertEquals(3L, summary.get("totalAlerts"));
        assertEquals(3L, summary.get("totalIncidents"));
        assertEquals(3L, summary.get("totalAnomalies"));
        assertEquals(30.0, summary.get("networkAverageSpeedKmh"));
    }
}
