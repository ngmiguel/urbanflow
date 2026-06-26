package com.urbanflow.analytics.domain.repository;

import com.urbanflow.analytics.domain.model.ZoneKpi;

import java.util.List;
import java.util.Optional;

public interface ZoneKpiRepository {

    ZoneKpi save(ZoneKpi zoneKpi);

    Optional<ZoneKpi> findByZoneId(String zoneId);

    List<ZoneKpi> findAll();
}
