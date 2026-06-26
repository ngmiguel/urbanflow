package com.urbanflow.alert.domain.model;

import com.urbanflow.common.exception.BusinessException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AlertTest {

    @Test
    void shouldTriggerActiveAlert() {
        Alert alert = Alert.trigger(
                UUID.randomUUID(),
                "TRAFFIC_ANOMALY",
                AlertSeverity.HIGH,
                "Speed dropped below threshold",
                "zone-casa-centre",
                "evt-1"
        );

        assertEquals(AlertStatus.ACTIVE, alert.getStatus());
        assertEquals("TRAFFIC_ANOMALY", alert.getAlertType());
    }

    @Test
    void shouldDismissAlert() {
        Alert alert = Alert.trigger(
                UUID.randomUUID(),
                "TRAFFIC_ANOMALY",
                AlertSeverity.HIGH,
                "Speed dropped below threshold",
                "zone-casa-centre",
                "evt-1"
        );

        alert.dismiss();

        assertEquals(AlertStatus.DISMISSED, alert.getStatus());
    }

    @Test
    void shouldRejectAcknowledgementAfterDismissal() {
        Alert alert = Alert.trigger(
                UUID.randomUUID(),
                "TRAFFIC_ANOMALY",
                AlertSeverity.HIGH,
                "Speed dropped below threshold",
                "zone-casa-centre",
                "evt-1"
        );
        alert.dismiss();

        assertThrows(BusinessException.class, alert::acknowledge);
    }
}
