package com.urbanflow.twin.domain.model;

import com.urbanflow.common.enums.CongestionLevel;

import java.time.Instant;

public record TwinZoneState(
        String zoneId,
        CongestionLevel congestionLevel,
        double averageSpeedKmh,
        int vehicleCount,
        int activeIncidents,
        Instant lastUpdated
) {

    public static TwinZoneState baseline(String zoneId) {
        return new TwinZoneState(
                zoneId,
                CongestionLevel.FREE_FLOW,
                50.0,
                100,
                0,
                Instant.now()
        );
    }
}
