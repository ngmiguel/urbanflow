package com.urbanflow.iot.domain.repository;

import com.urbanflow.iot.domain.model.IoTDevice;

import java.util.List;
import java.util.Optional;

public interface IoTDeviceRepository {

    IoTDevice save(IoTDevice device);

    Optional<IoTDevice> findById(String deviceId);

    boolean existsById(String deviceId);

    List<IoTDevice> findByZoneId(String zoneId);

    List<IoTDevice> findAll(int page, int size);

    long count();
}
