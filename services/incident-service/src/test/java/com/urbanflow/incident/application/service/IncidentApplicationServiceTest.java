package com.urbanflow.incident.application.service;

import com.urbanflow.incident.application.port.IncidentEventPublisher;
import com.urbanflow.incident.domain.model.Incident;
import com.urbanflow.incident.domain.model.IncidentSeverity;
import com.urbanflow.incident.domain.model.IncidentStatus;
import com.urbanflow.incident.domain.model.IncidentType;
import com.urbanflow.incident.domain.repository.IncidentRepository;
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
class IncidentApplicationServiceTest {

    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private IncidentEventPublisher incidentEventPublisher;

    @InjectMocks
    private IncidentApplicationService incidentApplicationService;

    @Test
    void shouldReportIncidentAndPublishEvent() {
        Incident incident = new Incident(
                UUID.randomUUID(),
                IncidentType.ACCIDENT,
                IncidentSeverity.HIGH,
                IncidentStatus.OPEN,
                "Multi-vehicle collision",
                "zone-casa-centre",
                33.5731,
                -7.5898,
                "operator-1",
                null,
                Instant.now(),
                Instant.now()
        );

        when(incidentRepository.save(any())).thenReturn(incident);

        var result = incidentApplicationService.reportIncident(
                incident.getType(),
                incident.getSeverity(),
                incident.getDescription(),
                incident.getZoneId(),
                incident.getLatitude(),
                incident.getLongitude(),
                incident.getReportedBy(),
                "corr-1"
        );

        assertEquals(incident.getDescription(), result.description());
        verify(incidentEventPublisher).publish(any());
    }
}
