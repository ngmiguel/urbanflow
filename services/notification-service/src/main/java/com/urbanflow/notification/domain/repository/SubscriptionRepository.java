package com.urbanflow.notification.domain.repository;

import com.urbanflow.notification.domain.model.Subscription;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository {

    Subscription save(Subscription subscription);

    Optional<Subscription> findById(UUID id);

    List<Subscription> findEnabledByZoneId(String zoneId);

    List<Subscription> findByUserId(String userId);

    void deleteById(UUID id);
}
