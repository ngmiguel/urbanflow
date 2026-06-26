package com.urbanflow.event.infrastructure.persistence;

import com.urbanflow.event.domain.model.UrbanEvent;
import com.urbanflow.event.domain.model.UrbanEventStatus;
import com.urbanflow.event.domain.repository.UrbanEventRepository;
import com.urbanflow.event.infrastructure.persistence.mapper.UrbanEventMapper;
import com.urbanflow.event.infrastructure.persistence.repository.UrbanEventJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UrbanEventRepositoryImpl implements UrbanEventRepository {

    private final UrbanEventJpaRepository jpaRepository;

    public UrbanEventRepositoryImpl(UrbanEventJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public UrbanEvent save(UrbanEvent event) {
        return UrbanEventMapper.toDomain(jpaRepository.save(UrbanEventMapper.toEntity(event)));
    }

    @Override
    public Optional<UrbanEvent> findById(UUID id) {
        return jpaRepository.findById(id).map(UrbanEventMapper::toDomain);
    }

    @Override
    public List<UrbanEvent> findAll(int page, int size) {
        return jpaRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startsAt")))
                .stream()
                .map(UrbanEventMapper::toDomain)
                .toList();
    }

    @Override
    public List<UrbanEvent> findByZoneId(String zoneId) {
        return jpaRepository.findByZoneIdOrderByStartsAtAsc(zoneId).stream()
                .map(UrbanEventMapper::toDomain)
                .toList();
    }

    @Override
    public List<UrbanEvent> findUpcomingByZoneId(String zoneId, Instant from, int limit) {
        return jpaRepository.findByZoneIdAndStatusInAndStartsAtAfterOrderByStartsAtAsc(
                zoneId,
                List.of(UrbanEventStatus.SCHEDULED, UrbanEventStatus.ACTIVE),
                from,
                PageRequest.of(0, limit)
        ).stream().map(UrbanEventMapper::toDomain).toList();
    }

    @Override
    public List<UrbanEvent> findByStatus(UrbanEventStatus status) {
        return jpaRepository.findByStatus(status).stream().map(UrbanEventMapper::toDomain).toList();
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}
