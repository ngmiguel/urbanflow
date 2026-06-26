package com.urbanflow.twin.application.port;

import com.urbanflow.twin.domain.model.TwinZoneState;

import java.util.Optional;

public interface TwinStateStore {

    Optional<TwinZoneState> getZoneState(String zoneId);

    void saveZoneState(TwinZoneState state);

    void saveProjectedState(String zoneId, TwinZoneState projectedState);
}
