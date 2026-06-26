package com.urbanflow.notification.infrastructure.persistence.repository;

import com.urbanflow.notification.infrastructure.persistence.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity, UUID> {

    @Query("""
            SELECT s FROM SubscriptionEntity s
            WHERE s.enabled = true AND (s.zoneId = :zoneId OR s.zoneId IS NULL)
            """)
    List<SubscriptionEntity> findEnabledForZone(@Param("zoneId") String zoneId);

    List<SubscriptionEntity> findByUserIdOrderByCreatedAtDesc(String userId);
}
