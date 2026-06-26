package com.urbanflow.websocket.application.dto;

import com.urbanflow.events.EventMetadata;
import com.urbanflow.events.notification.NotificationOutboxEvent;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RealtimeNotificationMessageTest {

    @Test
    void shouldMapFromOutboxEvent() {
        UUID notificationId = UUID.randomUUID();
        NotificationOutboxEvent event = new NotificationOutboxEvent(
                EventMetadata.create("NOTIFICATION_DISPATCH", "notification-service", "corr-1"),
                notificationId,
                "user-1",
                "/topic/alerts",
                "Traffic alert",
                "Heavy congestion detected",
                "HIGH",
                "zone-casa-centre",
                "ALERT"
        );

        RealtimeNotificationMessage message = RealtimeNotificationMessage.from(event);

        assertEquals(notificationId, message.notificationId());
        assertEquals("user-1", message.userId());
        assertEquals("Traffic alert", message.title());
        assertEquals("zone-casa-centre", message.zoneId());
        assertNotNull(message.occurredAt());
    }
}
