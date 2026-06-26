package com.urbanflow.event.infrastructure.persistence.repository;

import com.urbanflow.event.domain.model.UrbanEventStatus;
import com.urbanflow.event.infrastructure.persistence.entity.UrbanEventEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UrbanEventJpaRepository extends JpaRepository<UrbanEventEntity, UUID> {

    List<UrbanEventEntity> findByZoneIdOrderByStartsAtAsc(String zoneId);

    List<UrbanEventEntity> findByZoneIdAndStatusInAndStartsAtAfterOrderByStartsAtAsc(
            String zoneId,
            List<UrbanEventStatus> statuses,
            Instant from,
            Pageable pageable
    );

    List<UrbanEventEntity> findByStatus(UrbanEventStatus status);
}
