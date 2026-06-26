package com.urbanflow.iot.infrastructure.persistence.repository;

import com.urbanflow.iot.infrastructure.persistence.entity.IoTDeviceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IoTDeviceJpaRepository extends JpaRepository<IoTDeviceEntity, String> {

    List<IoTDeviceEntity> findByZoneIdOrderByNameAsc(String zoneId);

    List<IoTDeviceEntity> findAllBy(Pageable pageable);
}
