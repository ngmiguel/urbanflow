package com.urbanflow.notification.domain.repository;

import com.urbanflow.notification.domain.model.Notification;
import com.urbanflow.notification.domain.model.NotificationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository {

    Notification save(Notification notification);

    Optional<Notification> findById(UUID id);

    List<Notification> findByUserId(String userId, int page, int size);

    long countByUserId(String userId);
}
