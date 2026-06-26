package com.urbanflow.analytics.application.service;

import com.urbanflow.analytics.application.port.EventIdempotencyStore;
import com.urbanflow.analytics.domain.model.ZoneKpi;
import com.urbanflow.analytics.domain.repository.MetricHistoryRepository;
import com.urbanflow.analytics.domain.repository.ZoneKpiRepository;
import com.urbanflow.common.enums.CongestionLevel;
import com.urbanflow.events.EventMetadata;
import com.urbanflow.events.traffic.TrafficEventType;
import com.urbanflow.events.traffic.TrafficUpdateEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsIngestionServiceTest {

    @Mock
    private ZoneKpiRepository zoneKpiRepository;

    @Mock
    private MetricHistoryRepository metricHistoryRepository;

    @Mock
    private EventIdempotencyStore idempotencyStore;

    @InjectMocks
    private AnalyticsIngestionService analyticsIngestionService;

    @Test
    void shouldAggregateTrafficEvent() {
        UUID eventId = UUID.randomUUID();
        when(idempotencyStore.alreadyProcessed(eventId)).thenReturn(false);
        when(zoneKpiRepository.findByZoneId("zone-casa-centre")).thenReturn(Optional.empty());
        when(zoneKpiRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(metricHistoryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TrafficUpdateEvent event = new TrafficUpdateEvent(
                new EventMetadata(eventId, "TRAFFIC_UPDATE", "traffic-service", Instant.now(), "corr-1", 1),
                UUID.randomUUID(),
                "Boulevard Zerktouni",
                TrafficEventType.CONGESTION_CHANGED,
                CongestionLevel.HEAVY,
                18.0,
                520,
                "zone-casa-centre"
        );

        analyticsIngestionService.ingestTrafficEvent(event);

        verify(zoneKpiRepository).save(any(ZoneKpi.class));
        verify(idempotencyStore).markProcessed(eventId);
    }
}
