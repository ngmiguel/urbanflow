package com.urbanflow.iot.domain.model;

import com.urbanflow.common.exception.BusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IoTDeviceTest {

    @Test
    void shouldRegisterDeviceWithNormalizedId() {
        IoTDevice device = IoTDevice.register(
                "sensor-01",
                "Traffic Sensor",
                SensorType.TRAFFIC,
                "zone-casa-centre",
                33.57,
                -7.58
        );

        assertEquals("SENSOR-01", device.getDeviceId());
        assertEquals(DeviceStatus.OFFLINE, device.getStatus());
        assertNotNull(device.getCreatedAt());
        assertNull(device.getLastHeartbeatAt());
    }

    @Test
    void shouldMarkDeviceOnlineOnHeartbeat() {
        IoTDevice device = IoTDevice.register(
                "sensor-01",
                "Traffic Sensor",
                SensorType.TRAFFIC,
                "zone-casa-centre",
                33.57,
                -7.58
        );

        device.heartbeat();

        assertEquals(DeviceStatus.ONLINE, device.getStatus());
        assertNotNull(device.getLastHeartbeatAt());
    }

    @Test
    void shouldRejectTelemetryWhenOffline() {
        IoTDevice device = IoTDevice.register(
                "sensor-01",
                "Traffic Sensor",
                SensorType.TRAFFIC,
                "zone-casa-centre",
                33.57,
                -7.58
        );

        assertThrows(BusinessException.class, device::ensureOnline);
    }
}
