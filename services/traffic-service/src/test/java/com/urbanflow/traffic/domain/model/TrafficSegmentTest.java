package com.urbanflow.traffic.domain.model;

import com.urbanflow.common.enums.CongestionLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TrafficSegmentTest {

    @Test
    void shouldCreateSegmentWithDefaults() {
        TrafficSegment segment = TrafficSegment.create(
                "Avenue Mohammed V",
                "zone-casa-centre",
                CongestionLevel.MODERATE,
                35.0,
                300,
                33.57,
                -7.58
        );

        assertNotNull(segment.getId());
        assertEquals("Avenue Mohammed V", segment.getName());
        assertEquals(CongestionLevel.MODERATE, segment.getCongestionLevel());
    }

    @Test
    void shouldUpdateMetrics() {
        TrafficSegment segment = TrafficSegment.create(
                "Avenue Mohammed V",
                "zone-casa-centre",
                CongestionLevel.MODERATE,
                35.0,
                300,
                33.57,
                -7.58
        );

        segment.updateMetrics(CongestionLevel.HEAVY, 18.0, 420);

        assertEquals(CongestionLevel.HEAVY, segment.getCongestionLevel());
        assertEquals(18.0, segment.getAverageSpeedKmh());
        assertEquals(420, segment.getVehicleCount());
        assertNotNull(segment.getUpdatedAt());
    }
}
