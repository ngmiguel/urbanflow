package com.urbanflow.auth.domain.repository;

import com.urbanflow.auth.domain.model.RefreshToken;
import com.urbanflow.auth.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
