package com.urbanflow.notification.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NotificationTest {

    @Test
    void shouldCreateUnreadNotification() {
        Notification notification = Notification.create(
                "user-1",
                NotificationType.ALERT,
                "Traffic alert",
                "Heavy congestion detected",
                "HIGH",
                "zone-casa-centre",
                "evt-1"
        );

        assertEquals(NotificationStatus.UNREAD, notification.getStatus());
        assertEquals("user-1", notification.getUserId());
        assertNotNull(notification.getCreatedAt());
    }

    @Test
    void shouldMarkNotificationAsRead() {
        Notification notification = Notification.create(
                "user-1",
                NotificationType.ALERT,
                "Traffic alert",
                "Heavy congestion detected",
                "HIGH",
                "zone-casa-centre",
                "evt-1"
        );

        notification.markRead();

        assertEquals(NotificationStatus.READ, notification.getStatus());
        assertNotNull(notification.getReadAt());
    }
}
