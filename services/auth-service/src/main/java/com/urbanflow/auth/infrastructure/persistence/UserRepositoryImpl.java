package com.urbanflow.auth.infrastructure.persistence;

import com.urbanflow.auth.domain.model.User;
import com.urbanflow.auth.domain.repository.UserRepository;
import com.urbanflow.auth.infrastructure.persistence.mapper.AuthPersistenceMapper;
import com.urbanflow.auth.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(User user) {
        return AuthPersistenceMapper.toDomain(
                userJpaRepository.save(AuthPersistenceMapper.toEntity(user))
        );
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id).map(AuthPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(AuthPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
}
