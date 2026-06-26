package com.urbanflow.twin.domain.model;

import com.urbanflow.common.enums.CongestionLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TwinZoneStateTest {

    @Test
    void shouldCreateBaselineState() {
        TwinZoneState state = TwinZoneState.baseline("zone-casa-centre");

        assertEquals("zone-casa-centre", state.zoneId());
        assertEquals(CongestionLevel.FREE_FLOW, state.congestionLevel());
        assertEquals(50.0, state.averageSpeedKmh());
        assertEquals(0, state.activeIncidents());
        assertNotNull(state.lastUpdated());
    }
}
