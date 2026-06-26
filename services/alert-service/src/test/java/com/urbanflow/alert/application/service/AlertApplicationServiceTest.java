package com.urbanflow.alert.application.service;

import com.urbanflow.alert.domain.model.Alert;
import com.urbanflow.alert.domain.model.AlertSeverity;
import com.urbanflow.alert.domain.model.AlertStatus;
import com.urbanflow.alert.domain.repository.AlertRepository;
import com.urbanflow.common.exception.BusinessException;
import com.urbanflow.common.exception.ResourceNotFoundException;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlertApplicationServiceTest {

    @Mock
    private AlertRepository alertRepository;

    @InjectMocks
    private AlertApplicationService alertApplicationService;

    @Test
    void shouldAcknowledgeActiveAlert() {
        UUID alertId = UUID.randomUUID();
        Alert alert = Alert.trigger(
                UUID.randomUUID(),
                "TRAFFIC_ANOMALY",
                AlertSeverity.HIGH,
                "Speed dropped below threshold",
                "zone-casa-centre",
                "evt-1"
        );
        alert.setId(alertId);

        when(alertRepository.findById(alertId)).thenReturn(Optional.of(alert));
        when(alertRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = alertApplicationService.acknowledgeAlert(alertId);

        assertEquals(AlertStatus.ACKNOWLEDGED, result.status());
    }

    @Test
    void shouldRejectAcknowledgementForDismissedAlert() {
        UUID alertId = UUID.randomUUID();
        Alert alert = Alert.trigger(
                UUID.randomUUID(),
                "TRAFFIC_ANOMALY",
                AlertSeverity.HIGH,
                "Speed dropped below threshold",
                "zone-casa-centre",
                "evt-1"
        );
        alert.setId(alertId);
        alert.dismiss();

        when(alertRepository.findById(alertId)).thenReturn(Optional.of(alert));

        assertThrows(BusinessException.class, () -> alertApplicationService.acknowledgeAlert(alertId));
    }

    @Test
    void shouldThrowWhenAlertMissing() {
        UUID alertId = UUID.randomUUID();
        when(alertRepository.findById(alertId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> alertApplicationService.getAlert(alertId));
    }
}
