package com.urbanflow.notification.application.service;

import com.urbanflow.common.dto.PageMeta;
import com.urbanflow.common.exception.ResourceNotFoundException;
import com.urbanflow.notification.application.dto.NotificationDto;
import com.urbanflow.notification.application.dto.SubscriptionDto;
import com.urbanflow.notification.domain.model.Notification;
import com.urbanflow.notification.domain.model.Subscription;
import com.urbanflow.notification.domain.repository.NotificationRepository;
import com.urbanflow.notification.domain.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationApplicationService {

    private final NotificationRepository notificationRepository;
    private final SubscriptionRepository subscriptionRepository;

    public NotificationApplicationService(
            NotificationRepository notificationRepository,
            SubscriptionRepository subscriptionRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional(readOnly = true)
    public NotificationDto getNotification(UUID id, String userId) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", id));
        if (!notification.getUserId().equals(userId)
                && !NotificationDispatchService.BROADCAST_USER.equals(notification.getUserId())) {
            throw new ResourceNotFoundException("Notification", id);
        }
        return toDto(notification);
    }

    @Transactional(readOnly = true)
    public PagedNotifications getNotificationsForUser(String userId, int page, int size) {
        List<NotificationDto> notifications = notificationRepository.findByUserId(userId, page, size).stream()
                .map(this::toDto)
                .toList();
        long total = notificationRepository.countByUserId(userId);
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) total / size);
        return new PagedNotifications(notifications, new PageMeta(page, size, total, totalPages));
    }

    @Transactional
    public NotificationDto markAsRead(UUID id, String userId) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", id));
        if (!notification.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Notification", id);
        }
        notification.markRead();
        return toDto(notificationRepository.save(notification));
    }

    @Transactional
    public SubscriptionDto subscribe(String userId, String zoneId) {
        Subscription saved = subscriptionRepository.save(Subscription.create(userId, zoneId));
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<SubscriptionDto> getSubscriptions(String userId) {
        return subscriptionRepository.findByUserId(userId).stream().map(this::toDto).toList();
    }

    @Transactional
    public void unsubscribe(UUID id, String userId) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", id));
        if (!subscription.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Subscription", id);
        }
        subscriptionRepository.deleteById(id);
    }

    private NotificationDto toDto(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getUserId(),
                notification.getType(),
                notification.getStatus(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getSeverity(),
                notification.getZoneId(),
                notification.getSourceEventId(),
                notification.getCreatedAt(),
                notification.getReadAt()
        );
    }

    private SubscriptionDto toDto(Subscription subscription) {
        return new SubscriptionDto(
                subscription.getId(),
                subscription.getUserId(),
                subscription.getZoneId(),
                subscription.isEnabled(),
                subscription.getCreatedAt()
        );
    }

    public record PagedNotifications(List<NotificationDto> content, PageMeta page) {
    }
}
