package com.urbanflow.traffic.application.service;

import com.urbanflow.common.enums.CongestionLevel;
import com.urbanflow.traffic.application.port.TrafficEventPublisher;
import com.urbanflow.traffic.domain.model.TrafficSegment;
import com.urbanflow.traffic.domain.repository.TrafficSegmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrafficApplicationServiceTest {

    @Mock
    private TrafficSegmentRepository segmentRepository;

    @Mock
    private TrafficEventPublisher eventPublisher;

    @InjectMocks
    private TrafficApplicationService trafficApplicationService;

    @Test
    void shouldCreateSegmentAndPublishEvent() {
        TrafficSegment segment = new TrafficSegment(
                UUID.randomUUID(),
                "Avenue Mohammed V",
                "zone-casa-centre",
                CongestionLevel.MODERATE,
                35.0,
                120,
                33.5731,
                -7.5898,
                Instant.now(),
                Instant.now()
        );

        when(segmentRepository.save(any())).thenReturn(segment);

        var result = trafficApplicationService.createSegment(
                segment.getName(),
                segment.getZoneId(),
                segment.getCongestionLevel(),
                segment.getAverageSpeedKmh(),
                segment.getVehicleCount(),
                segment.getLatitude(),
                segment.getLongitude(),
                "corr-1"
        );

        assertEquals(segment.getName(), result.name());
        verify(eventPublisher).publish(any());
    }
}
