package com.urbanflow.iot.application.service;

import com.urbanflow.common.dto.PageMeta;
import com.urbanflow.common.exception.BusinessException;
import com.urbanflow.common.exception.ResourceNotFoundException;
import com.urbanflow.common.util.IdGenerator;
import com.urbanflow.events.EventMetadata;
import com.urbanflow.events.sensor.SensorRawEvent;
import com.urbanflow.iot.application.dto.IoTDeviceDto;
import com.urbanflow.iot.application.port.SensorEventPublisher;
import com.urbanflow.iot.domain.model.IoTDevice;
import com.urbanflow.iot.domain.model.SensorType;
import com.urbanflow.iot.domain.repository.IoTDeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IoTDeviceApplicationService {

    private final IoTDeviceRepository deviceRepository;
    private final SensorEventPublisher sensorEventPublisher;

    public IoTDeviceApplicationService(
            IoTDeviceRepository deviceRepository,
            SensorEventPublisher sensorEventPublisher
    ) {
        this.deviceRepository = deviceRepository;
        this.sensorEventPublisher = sensorEventPublisher;
    }

    @Transactional
    public IoTDeviceDto registerDevice(
            String deviceId,
            String name,
            SensorType sensorType,
            String zoneId,
            double latitude,
            double longitude
    ) {
        String normalizedId = deviceId.toUpperCase();
        if (deviceRepository.existsById(normalizedId)) {
            throw new BusinessException("DEVICE_ALREADY_EXISTS", "Device already registered: " + normalizedId);
        }

        IoTDevice saved = deviceRepository.save(
                IoTDevice.register(normalizedId, name, sensorType, zoneId, latitude, longitude)
        );
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public IoTDeviceDto getDevice(String deviceId) {
        return deviceRepository.findById(deviceId.toUpperCase())
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("IoTDevice", deviceId));
    }

    @Transactional(readOnly = true)
    public List<IoTDeviceDto> getDevicesByZone(String zoneId) {
        return deviceRepository.findByZoneId(zoneId).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public PagedDevices getAllDevices(int page, int size) {
        List<IoTDeviceDto> devices = deviceRepository.findAll(page, size).stream()
                .map(this::toDto)
                .toList();
        long total = deviceRepository.count();
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) total / size);
        return new PagedDevices(devices, new PageMeta(page, size, total, totalPages));
    }

    @Transactional
    public IoTDeviceDto heartbeat(String deviceId) {
        IoTDevice device = loadDevice(deviceId);
        device.heartbeat();
        return toDto(deviceRepository.save(device));
    }

    @Transactional
    public IoTDeviceDto submitTelemetry(
            String deviceId,
            double value,
            String unit,
            String correlationId
    ) {
        IoTDevice device = loadDevice(deviceId);
        device.ensureOnline();

        String corrId = correlationId != null && !correlationId.isBlank()
                ? correlationId
                : IdGenerator.newCorrelationId();

        SensorRawEvent event = new SensorRawEvent(
                EventMetadata.create("SENSOR_READING", "iot-device-service", corrId),
                device.getDeviceId(),
                device.getSensorType().name(),
                value,
                unit,
                device.getLatitude(),
                device.getLongitude(),
                device.getZoneId()
        );
        sensorEventPublisher.publish(event);
        return toDto(device);
    }

    private IoTDevice loadDevice(String deviceId) {
        return deviceRepository.findById(deviceId.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("IoTDevice", deviceId));
    }

    private IoTDeviceDto toDto(IoTDevice device) {
        return new IoTDeviceDto(
                device.getDeviceId(),
                device.getName(),
                device.getSensorType(),
                device.getZoneId(),
                device.getLatitude(),
                device.getLongitude(),
                device.getStatus(),
                device.getLastHeartbeatAt(),
                device.getCreatedAt(),
                device.getUpdatedAt()
        );
    }

    public record PagedDevices(List<IoTDeviceDto> content, PageMeta page) {
    }
}
