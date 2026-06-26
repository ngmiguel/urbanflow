package com.urbanflow.iot.infrastructure.persistence.mapper;

import com.urbanflow.iot.domain.model.IoTDevice;
import com.urbanflow.iot.infrastructure.persistence.entity.IoTDeviceEntity;

public final class IoTDeviceMapper {

    private IoTDeviceMapper() {
    }

    public static IoTDeviceEntity toEntity(IoTDevice device) {
        IoTDeviceEntity entity = new IoTDeviceEntity();
        entity.setDeviceId(device.getDeviceId());
        entity.setName(device.getName());
        entity.setSensorType(device.getSensorType());
        entity.setZoneId(device.getZoneId());
        entity.setLatitude(device.getLatitude());
        entity.setLongitude(device.getLongitude());
        entity.setStatus(device.getStatus());
        entity.setLastHeartbeatAt(device.getLastHeartbeatAt());
        entity.setCreatedAt(device.getCreatedAt());
        entity.setUpdatedAt(device.getUpdatedAt());
        return entity;
    }

    public static IoTDevice toDomain(IoTDeviceEntity entity) {
        return new IoTDevice(
                entity.getDeviceId(),
                entity.getName(),
                entity.getSensorType(),
                entity.getZoneId(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getStatus(),
                entity.getLastHeartbeatAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
