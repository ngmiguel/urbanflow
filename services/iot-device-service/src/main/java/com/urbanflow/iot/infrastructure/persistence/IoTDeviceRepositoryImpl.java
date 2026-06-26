package com.urbanflow.iot.infrastructure.persistence;

import com.urbanflow.iot.domain.model.IoTDevice;
import com.urbanflow.iot.domain.repository.IoTDeviceRepository;
import com.urbanflow.iot.infrastructure.persistence.mapper.IoTDeviceMapper;
import com.urbanflow.iot.infrastructure.persistence.repository.IoTDeviceJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class IoTDeviceRepositoryImpl implements IoTDeviceRepository {

    private final IoTDeviceJpaRepository jpaRepository;

    public IoTDeviceRepositoryImpl(IoTDeviceJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public IoTDevice save(IoTDevice device) {
        return IoTDeviceMapper.toDomain(jpaRepository.save(IoTDeviceMapper.toEntity(device)));
    }

    @Override
    public Optional<IoTDevice> findById(String deviceId) {
        return jpaRepository.findById(deviceId).map(IoTDeviceMapper::toDomain);
    }

    @Override
    public boolean existsById(String deviceId) {
        return jpaRepository.existsById(deviceId);
    }

    @Override
    public List<IoTDevice> findByZoneId(String zoneId) {
        return jpaRepository.findByZoneIdOrderByNameAsc(zoneId).stream()
                .map(IoTDeviceMapper::toDomain)
                .toList();
    }

    @Override
    public List<IoTDevice> findAll(int page, int size) {
        return jpaRepository.findAllBy(PageRequest.of(page, size)).stream()
                .map(IoTDeviceMapper::toDomain)
                .toList();
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}
