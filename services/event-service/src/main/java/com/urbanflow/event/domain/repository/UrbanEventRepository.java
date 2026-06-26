package com.urbanflow.event.domain.repository;

import com.urbanflow.event.domain.model.UrbanEvent;
import com.urbanflow.event.domain.model.UrbanEventStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UrbanEventRepository {

    UrbanEvent save(UrbanEvent event);

    Optional<UrbanEvent> findById(UUID id);

    List<UrbanEvent> findAll(int page, int size);

    List<UrbanEvent> findByZoneId(String zoneId);

    List<UrbanEvent> findUpcomingByZoneId(String zoneId, Instant from, int limit);

    List<UrbanEvent> findByStatus(UrbanEventStatus status);

    long count();
}
