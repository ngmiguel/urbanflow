package com.urbanflow.analytics.domain.model;

import com.urbanflow.common.enums.CongestionLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ZoneKpiTest {

    @Test
    void shouldInitializeDefaultsForNewZone() {
        ZoneKpi kpi = new ZoneKpi("zone-casa-centre");

        assertEquals("zone-casa-centre", kpi.getZoneId());
        assertEquals(CongestionLevel.FREE_FLOW, kpi.getCongestionLevel());
        assertEquals(0.0, kpi.getAverageSpeedKmh());
    }

    @Test
    void shouldComputeAirQualityAverage() {
        ZoneKpi kpi = new ZoneKpi("zone-casa-centre");
        kpi.setAirQualitySum(150.0);
        kpi.setAirQualitySamples(3);

        assertEquals(50.0, kpi.getAirQualityAverage());
    }

    @Test
    void shouldReturnNullAirQualityAverageWithoutSamples() {
        ZoneKpi kpi = new ZoneKpi("zone-casa-centre");

        assertNull(kpi.getAirQualityAverage());
    }
}
