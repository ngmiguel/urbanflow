package com.urbanflow.traffic.domain.repository;

import com.urbanflow.traffic.domain.model.TrafficSegment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrafficSegmentRepository {

    TrafficSegment save(TrafficSegment segment);

    Optional<TrafficSegment> findById(UUID id);

    List<TrafficSegment> findByZoneId(String zoneId);

    List<TrafficSegment> findAll(int page, int size);

    long count();
}
