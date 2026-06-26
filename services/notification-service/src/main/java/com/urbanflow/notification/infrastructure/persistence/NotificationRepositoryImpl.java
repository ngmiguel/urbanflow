package com.urbanflow.notification.infrastructure.persistence;

import com.urbanflow.notification.domain.model.Notification;
import com.urbanflow.notification.domain.repository.NotificationRepository;
import com.urbanflow.notification.infrastructure.persistence.mapper.NotificationMapper;
import com.urbanflow.notification.infrastructure.persistence.repository.NotificationJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository jpaRepository;

    public NotificationRepositoryImpl(NotificationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Notification save(Notification notification) {
        return NotificationMapper.toDomain(jpaRepository.save(NotificationMapper.toEntity(notification)));
    }

    @Override
    public Optional<Notification> findById(UUID id) {
        return jpaRepository.findById(id).map(NotificationMapper::toDomain);
    }

    @Override
    public List<Notification> findByUserId(String userId, int page, int size) {
        return jpaRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, size)).stream()
                .map(NotificationMapper::toDomain)
                .toList();
    }

    @Override
    public long countByUserId(String userId) {
        return jpaRepository.countByUserId(userId);
    }
}
