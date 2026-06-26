package com.urbanflow.iot.application.service;

import com.urbanflow.common.exception.BusinessException;
import com.urbanflow.common.exception.ResourceNotFoundException;
import com.urbanflow.events.sensor.SensorRawEvent;
import com.urbanflow.iot.application.port.SensorEventPublisher;
import com.urbanflow.iot.domain.model.DeviceStatus;
import com.urbanflow.iot.domain.model.IoTDevice;
import com.urbanflow.iot.domain.model.SensorType;
import com.urbanflow.iot.domain.repository.IoTDeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IoTDeviceApplicationServiceTest {

    @Mock
    private IoTDeviceRepository deviceRepository;

    @Mock
    private SensorEventPublisher sensorEventPublisher;

    @InjectMocks
    private IoTDeviceApplicationService ioTDeviceApplicationService;

    @Test
    void shouldRegisterNewDevice() {
        when(deviceRepository.existsById("SENSOR-01")).thenReturn(false);
        when(deviceRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = ioTDeviceApplicationService.registerDevice(
                "sensor-01",
                "Traffic Sensor",
                SensorType.TRAFFIC,
                "zone-casa-centre",
                33.57,
                -7.58
        );

        assertEquals("SENSOR-01", result.deviceId());
        assertEquals(DeviceStatus.OFFLINE, result.status());
    }

    @Test
    void shouldRejectDuplicateRegistration() {
        when(deviceRepository.existsById("SENSOR-01")).thenReturn(true);

        assertThrows(BusinessException.class, () ->
                ioTDeviceApplicationService.registerDevice(
                        "sensor-01",
                        "Traffic Sensor",
                        SensorType.TRAFFIC,
                        "zone-casa-centre",
                        33.57,
                        -7.58
                ));
    }

    @Test
    void shouldPublishTelemetryForOnlineDevice() {
        IoTDevice device = IoTDevice.register(
                "sensor-01",
                "Traffic Sensor",
                SensorType.TRAFFIC,
                "zone-casa-centre",
                33.57,
                -7.58
        );
        device.heartbeat();

        when(deviceRepository.findById("SENSOR-01")).thenReturn(Optional.of(device));

        ioTDeviceApplicationService.submitTelemetry("sensor-01", 42.0, "km/h", "corr-1");

        verify(sensorEventPublisher).publish(any(SensorRawEvent.class));
    }

    @Test
    void shouldRejectTelemetryForMissingDevice() {
        when(deviceRepository.findById("SENSOR-99")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                ioTDeviceApplicationService.submitTelemetry("sensor-99", 42.0, "km/h", "corr-1"));

        verify(sensorEventPublisher, never()).publish(any());
    }
}
