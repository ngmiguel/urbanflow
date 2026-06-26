package com.urbanflow.notification.application.service;

import com.urbanflow.common.dto.PageMeta;
import com.urbanflow.common.exception.ResourceNotFoundException;
import com.urbanflow.common.util.IdGenerator;
import com.urbanflow.events.EventMetadata;
import com.urbanflow.events.alert.AlertTriggeredEvent;
import com.urbanflow.events.incident.IncidentEvent;
import com.urbanflow.events.notification.NotificationOutboxEvent;
import com.urbanflow.notification.application.dto.NotificationDto;
import com.urbanflow.notification.application.port.EventIdempotencyStore;
import com.urbanflow.notification.application.port.NotificationOutboxPublisher;
import com.urbanflow.notification.domain.model.Notification;
import com.urbanflow.notification.domain.model.NotificationType;
import com.urbanflow.notification.domain.model.Subscription;
import com.urbanflow.notification.domain.repository.NotificationRepository;
import com.urbanflow.notification.domain.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationDispatchService {

    public static final String BROADCAST_USER = "broadcast";
    public static final String TOPIC_ALERTS = "/topic/alerts";

    private final NotificationRepository notificationRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final NotificationOutboxPublisher outboxPublisher;
    private final EventIdempotencyStore idempotencyStore;

    public NotificationDispatchService(
            NotificationRepository notificationRepository,
            SubscriptionRepository subscriptionRepository,
            NotificationOutboxPublisher outboxPublisher,
            EventIdempotencyStore idempotencyStore
    ) {
        this.notificationRepository = notificationRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.outboxPublisher = outboxPublisher;
        this.idempotencyStore = idempotencyStore;
    }

    @Transactional
    public void processAlertEvent(AlertTriggeredEvent event) {
        if (shouldSkip(event.metadata().eventId())) {
            return;
        }
        dispatch(
                NotificationType.ALERT,
                "Alert: " + event.alertType(),
                event.message(),
                event.severity(),
                event.zoneId(),
                event.metadata().eventId().toString(),
                event.metadata().correlationId()
        );
        idempotencyStore.markProcessed(event.metadata().eventId());
    }

    @Transactional
    public void processIncidentEvent(IncidentEvent event) {
        if (shouldSkip(event.metadata().eventId())) {
            return;
        }
        if (event.eventType() == IncidentEvent.IncidentEventType.RESOLVED) {
            idempotencyStore.markProcessed(event.metadata().eventId());
            return;
        }
        dispatch(
                NotificationType.INCIDENT,
                "Incident: " + event.eventType().name(),
                event.description(),
                event.severity(),
                event.zoneId(),
                event.metadata().eventId().toString(),
                event.metadata().correlationId()
        );
        idempotencyStore.markProcessed(event.metadata().eventId());
    }

    private boolean shouldSkip(UUID eventId) {
        return idempotencyStore.alreadyProcessed(eventId);
    }

    private void dispatch(
            NotificationType type,
            String title,
            String message,
            String severity,
            String zoneId,
            String sourceEventId,
            String correlationId
    ) {
        String corrId = correlationId != null && !correlationId.isBlank()
                ? correlationId
                : IdGenerator.newCorrelationId();

        publishForUser(BROADCAST_USER, type, title, message, severity, zoneId, sourceEventId, corrId, TOPIC_ALERTS);
        publishForUser(BROADCAST_USER, type, title, message, severity, zoneId, sourceEventId, corrId,
                "/topic/zone/" + zoneId);

        subscriptionRepository.findEnabledByZoneId(zoneId).stream()
                .filter(sub -> sub.matchesZone(zoneId))
                .forEach(sub -> publishForUser(
                        sub.getUserId(),
                        type,
                        title,
                        message,
                        severity,
                        zoneId,
                        sourceEventId,
                        corrId,
                        "/topic/zone/" + zoneId
                ));
    }

    private void publishForUser(
            String userId,
            NotificationType type,
            String title,
            String message,
            String severity,
            String zoneId,
            String sourceEventId,
            String correlationId,
            String stompTopic
    ) {
        Notification saved = notificationRepository.save(
                Notification.create(userId, type, title, message, severity, zoneId, sourceEventId)
        );
        outboxPublisher.publish(new NotificationOutboxEvent(
                EventMetadata.create(type.name(), "notification-service", correlationId),
                saved.getId(),
                userId,
                stompTopic,
                title,
                message,
                severity,
                zoneId,
                type.name()
        ));
    }
}
