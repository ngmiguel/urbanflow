package com.urbanflow.notification.application.service;

import com.urbanflow.common.exception.ResourceNotFoundException;
import com.urbanflow.notification.domain.model.Notification;
import com.urbanflow.notification.domain.model.NotificationStatus;
import com.urbanflow.notification.domain.model.NotificationType;
import com.urbanflow.notification.domain.repository.NotificationRepository;
import com.urbanflow.notification.domain.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationApplicationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private NotificationApplicationService notificationApplicationService;

    @Test
    void shouldMarkNotificationAsReadForOwner() {
        UUID notificationId = UUID.randomUUID();
        Notification notification = Notification.create(
                "user-1",
                NotificationType.ALERT,
                "Traffic alert",
                "Heavy congestion detected",
                "HIGH",
                "zone-casa-centre",
                "evt-1"
        );
        notification.setId(notificationId);

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = notificationApplicationService.markAsRead(notificationId, "user-1");

        assertEquals(NotificationStatus.READ, result.status());
        verify(notificationRepository).save(notification);
    }

    @Test
    void shouldRejectReadForDifferentUser() {
        UUID notificationId = UUID.randomUUID();
        Notification notification = Notification.create(
                "user-1",
                NotificationType.ALERT,
                "Traffic alert",
                "Heavy congestion detected",
                "HIGH",
                "zone-casa-centre",
                "evt-1"
        );
        notification.setId(notificationId);

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

        assertThrows(ResourceNotFoundException.class, () ->
                notificationApplicationService.markAsRead(notificationId, "user-2"));
    }
}
