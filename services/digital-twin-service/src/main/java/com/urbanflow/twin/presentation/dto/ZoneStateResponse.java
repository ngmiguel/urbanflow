package com.urbanflow.twin.presentation.dto;

import com.urbanflow.twin.domain.model.TwinZoneState;

public record ZoneStateResponse(
        String zoneId,
        String congestionLevel,
        double averageSpeedKmh,
        int vehicleCount,
        int activeIncidents,
        String lastUpdated
) {

    public static ZoneStateResponse from(TwinZoneState state) {
        return new ZoneStateResponse(
                state.zoneId(),
                state.congestionLevel().name(),
                state.averageSpeedKmh(),
                state.vehicleCount(),
                state.activeIncidents(),
                state.lastUpdated().toString()
        );
    }
}
