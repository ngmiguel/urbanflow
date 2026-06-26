package com.urbanflow.notification.infrastructure.messaging;

import com.urbanflow.notification.application.service.NotificationDispatchService;
import com.urbanflow.common.constant.KafkaTopics;
import com.urbanflow.events.alert.AlertTriggeredEvent;
import com.urbanflow.events.incident.IncidentEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class DomainEventListeners {

    private final NotificationDispatchService notificationDispatchService;

    public DomainEventListeners(NotificationDispatchService notificationDispatchService) {
        this.notificationDispatchService = notificationDispatchService;
    }

    @KafkaListener(
            topics = KafkaTopics.ALERT_EVENTS,
            groupId = "notification-service-alerts",
            containerFactory = "alertKafkaListenerContainerFactory"
    )
    public void onAlertEvent(AlertTriggeredEvent event) {
        notificationDispatchService.processAlertEvent(event);
    }

    @KafkaListener(
            topics = KafkaTopics.INCIDENT_EVENTS,
            groupId = "notification-service-incidents",
            containerFactory = "incidentKafkaListenerContainerFactory"
    )
    public void onIncidentEvent(IncidentEvent event) {
        notificationDispatchService.processIncidentEvent(event);
    }
}
