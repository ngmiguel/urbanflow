package com.urbanflow.notification.infrastructure.persistence;

import com.urbanflow.notification.domain.model.Subscription;
import com.urbanflow.notification.domain.repository.SubscriptionRepository;
import com.urbanflow.notification.infrastructure.persistence.mapper.NotificationMapper;
import com.urbanflow.notification.infrastructure.persistence.repository.SubscriptionJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private final SubscriptionJpaRepository jpaRepository;

    public SubscriptionRepositoryImpl(SubscriptionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Subscription save(Subscription subscription) {
        return NotificationMapper.toDomain(jpaRepository.save(NotificationMapper.toEntity(subscription)));
    }

    @Override
    public Optional<Subscription> findById(UUID id) {
        return jpaRepository.findById(id).map(NotificationMapper::toDomain);
    }

    @Override
    public List<Subscription> findEnabledByZoneId(String zoneId) {
        return jpaRepository.findEnabledForZone(zoneId).stream()
                .map(NotificationMapper::toDomain)
                .toList();
    }

    @Override
    public List<Subscription> findByUserId(String userId) {
        return jpaRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(NotificationMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
