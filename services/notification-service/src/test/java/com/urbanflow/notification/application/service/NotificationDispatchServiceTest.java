package com.urbanflow.notification.application.service;

import com.urbanflow.notification.application.port.EventIdempotencyStore;
import com.urbanflow.notification.application.port.NotificationOutboxPublisher;
import com.urbanflow.notification.domain.repository.NotificationRepository;
import com.urbanflow.notification.domain.repository.SubscriptionRepository;
import com.urbanflow.events.EventMetadata;
import com.urbanflow.events.alert.AlertTriggeredEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationDispatchServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private NotificationOutboxPublisher outboxPublisher;

    @Mock
    private EventIdempotencyStore idempotencyStore;

    @InjectMocks
    private NotificationDispatchService notificationDispatchService;

    @Test
    void shouldDispatchAlertNotification() {
        UUID eventId = UUID.randomUUID();
        when(idempotencyStore.alreadyProcessed(eventId)).thenReturn(false);
        when(subscriptionRepository.findEnabledByZoneId("zone-casa-centre")).thenReturn(List.of());
        when(notificationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AlertTriggeredEvent event = new AlertTriggeredEvent(
                new EventMetadata(eventId, "ALERT", "alert-service", Instant.now(), "corr-1", 1),
                UUID.randomUUID(),
                "TRAFFIC_CONGESTION",
                "HIGH",
                "Heavy congestion detected",
                "zone-casa-centre",
                "source-1"
        );

        notificationDispatchService.processAlertEvent(event);

        verify(outboxPublisher, atLeastOnce()).publish(any());
        verify(idempotencyStore).markProcessed(eventId);
    }
}
