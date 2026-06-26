package com.urbanflow.notification.infrastructure.persistence.mapper;

import com.urbanflow.notification.domain.model.Notification;
import com.urbanflow.notification.domain.model.Subscription;
import com.urbanflow.notification.infrastructure.persistence.entity.NotificationEntity;
import com.urbanflow.notification.infrastructure.persistence.entity.SubscriptionEntity;

public final class NotificationMapper {

    private NotificationMapper() {
    }

    public static Notification toDomain(NotificationEntity entity) {
        return new Notification(
                entity.getId(),
                entity.getUserId(),
                entity.getType(),
                entity.getStatus(),
                entity.getTitle(),
                entity.getMessage(),
                entity.getSeverity(),
                entity.getZoneId(),
                entity.getSourceEventId(),
                entity.getCreatedAt(),
                entity.getReadAt()
        );
    }

    public static NotificationEntity toEntity(Notification notification) {
        NotificationEntity entity = new NotificationEntity();
        entity.setId(notification.getId());
        entity.setUserId(notification.getUserId());
        entity.setType(notification.getType());
        entity.setStatus(notification.getStatus());
        entity.setTitle(notification.getTitle());
        entity.setMessage(notification.getMessage());
        entity.setSeverity(notification.getSeverity());
        entity.setZoneId(notification.getZoneId());
        entity.setSourceEventId(notification.getSourceEventId());
        entity.setCreatedAt(notification.getCreatedAt());
        entity.setReadAt(notification.getReadAt());
        return entity;
    }

    public static Subscription toDomain(SubscriptionEntity entity) {
        return new Subscription(
                entity.getId(),
                entity.getUserId(),
                entity.getZoneId(),
                entity.isEnabled(),
                entity.getCreatedAt()
        );
    }

    public static SubscriptionEntity toEntity(Subscription subscription) {
        SubscriptionEntity entity = new SubscriptionEntity();
        entity.setId(subscription.getId());
        entity.setUserId(subscription.getUserId());
        entity.setZoneId(subscription.getZoneId());
        entity.setEnabled(subscription.isEnabled());
        entity.setCreatedAt(subscription.getCreatedAt());
        return entity;
    }
}
