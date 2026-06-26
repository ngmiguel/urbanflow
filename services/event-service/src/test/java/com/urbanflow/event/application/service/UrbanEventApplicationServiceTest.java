package com.urbanflow.event.application.service;

import com.urbanflow.event.application.port.UrbanEventPublisher;
import com.urbanflow.event.domain.model.UrbanEvent;
import com.urbanflow.event.domain.model.UrbanEventStatus;
import com.urbanflow.event.domain.model.UrbanEventType;
import com.urbanflow.event.domain.repository.UrbanEventRepository;
import com.urbanflow.events.urban.UrbanPlannedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrbanEventApplicationServiceTest {

    @Mock
    private UrbanEventRepository urbanEventRepository;

    @Mock
    private UrbanEventPublisher urbanEventPublisher;

    @InjectMocks
    private UrbanEventApplicationService urbanEventApplicationService;

    @Test
    void shouldScheduleEventAndPublishKafkaMessage() {
        Instant startsAt = Instant.now().plus(1, ChronoUnit.DAYS);
        Instant endsAt = startsAt.plus(3, ChronoUnit.HOURS);

        when(urbanEventRepository.save(any())).thenAnswer(invocation -> {
            UrbanEvent event = invocation.getArgument(0);
            event.setId(UUID.randomUUID());
            return event;
        });

        var result = urbanEventApplicationService.scheduleEvent(
                UrbanEventType.CONCERT,
                "Jazz Night",
                "Open-air concert",
                "zone-casa-centre",
                33.57,
                -7.58,
                startsAt,
                endsAt,
                5000,
                "City Culture Office",
                "corr-1"
        );

        assertEquals(UrbanEventStatus.SCHEDULED, result.status());
        assertEquals("Jazz Night", result.title());
        verify(urbanEventPublisher).publish(any(UrbanPlannedEvent.class));
    }
}
