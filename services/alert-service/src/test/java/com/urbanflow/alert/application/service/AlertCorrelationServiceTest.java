package com.urbanflow.alert.application.service;

import com.urbanflow.alert.application.port.AlertEventPublisher;
import com.urbanflow.alert.application.port.AnomalyEventPublisher;
import com.urbanflow.alert.application.port.EventIdempotencyStore;
import com.urbanflow.alert.domain.model.AlertRule;
import com.urbanflow.alert.domain.model.AlertSeverity;
import com.urbanflow.alert.domain.model.RuleSourceType;
import com.urbanflow.alert.domain.repository.AlertRepository;
import com.urbanflow.alert.domain.repository.AlertRuleRepository;
import com.urbanflow.events.EventMetadata;
import com.urbanflow.events.incident.IncidentEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlertCorrelationServiceTest {

    @Mock
    private AlertRuleRepository alertRuleRepository;

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private AlertEventPublisher alertEventPublisher;

    @Mock
    private AnomalyEventPublisher anomalyEventPublisher;

    @Mock
    private EventIdempotencyStore idempotencyStore;

    @InjectMocks
    private AlertCorrelationService alertCorrelationService;

    @Test
    void shouldTriggerAlertWhenIncidentMatchesRule() {
        UUID eventId = UUID.randomUUID();
        UUID incidentId = UUID.randomUUID();

        when(idempotencyStore.alreadyProcessed(eventId)).thenReturn(false);
        when(alertRuleRepository.findBySourceTypeAndEnabledTrue(RuleSourceType.INCIDENT)).thenReturn(List.of(
                AlertRule.create(
                        "High severity incidents",
                        RuleSourceType.INCIDENT,
                        "zone-casa-centre",
                        AlertSeverity.HIGH,
                        AlertSeverity.HIGH,
                        null,
                        null,
                        null
                )
        ));
        when(alertRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        IncidentEvent event = new IncidentEvent(
                new EventMetadata(eventId, "REPORTED", "incident-service", Instant.now(), "corr-1", 1),
                incidentId,
                IncidentEvent.IncidentEventType.REPORTED,
                "HIGH",
                "Accident on highway",
                33.57,
                -7.58,
                "zone-casa-centre"
        );

        alertCorrelationService.processIncidentEvent(event);

        verify(alertEventPublisher).publish(any());
        verify(idempotencyStore).markProcessed(eventId);
    }

    @Test
    void shouldSkipResolvedIncidents() {
        UUID eventId = UUID.randomUUID();

        when(idempotencyStore.alreadyProcessed(eventId)).thenReturn(false);

        IncidentEvent event = new IncidentEvent(
                new EventMetadata(eventId, "RESOLVED", "incident-service", Instant.now(), "corr-1", 1),
                UUID.randomUUID(),
                IncidentEvent.IncidentEventType.RESOLVED,
                "LOW",
                "Resolved",
                33.57,
                -7.58,
                "zone-casa-centre"
        );

        alertCorrelationService.processIncidentEvent(event);

        verify(alertEventPublisher, never()).publish(any());
        verify(idempotencyStore).markProcessed(eventId);
    }
}
