package com.urbanflow.event.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UrbanEventTest {

    @Test
    void shouldScheduleEvent() {
        Instant startsAt = Instant.now().plus(1, ChronoUnit.DAYS);
        Instant endsAt = startsAt.plus(3, ChronoUnit.HOURS);

        UrbanEvent event = UrbanEvent.schedule(
                UrbanEventType.CONCERT,
                "Jazz Night",
                "Open-air concert",
                "zone-casa-centre",
                33.57,
                -7.58,
                startsAt,
                endsAt,
                5000,
                "City Culture Office"
        );

        assertEquals(UrbanEventStatus.SCHEDULED, event.getStatus());
        assertEquals(UrbanEventType.CONCERT, event.getType());
    }

    @Test
    void shouldRejectInvalidWindow() {
        Instant startsAt = Instant.now().plus(1, ChronoUnit.DAYS);
        Instant endsAt = startsAt.minus(1, ChronoUnit.HOURS);

        assertThrows(
                com.urbanflow.common.exception.BusinessException.class,
                () -> UrbanEvent.schedule(
                        UrbanEventType.MARKET,
                        "Farmers Market",
                        "Weekly market",
                        "zone-casa-centre",
                        33.57,
                        -7.58,
                        startsAt,
                        endsAt,
                        1000,
                        "Markets Dept"
                )
        );
    }
}
