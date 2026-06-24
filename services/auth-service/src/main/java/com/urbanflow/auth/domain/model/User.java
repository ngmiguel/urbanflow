package com.urbanflow.auth.domain.model;

import com.urbanflow.common.enums.UserRole;

import java.time.Instant;
import java.util.UUID;

public class User {

    private UUID id;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private UserRole role;
    private boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;

    public User() {
    }

    public User(
            UUID id,
            String email,
            String passwordHash,
            String firstName,
            String lastName,
            UserRole role,
            boolean enabled,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.enabled = enabled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User createNew(
            String email,
            String passwordHash,
            String firstName,
            String lastName,
            UserRole role
    ) {
        Instant now = Instant.now();
        return new User(
                UUID.randomUUID(),
                email.toLowerCase(),
                passwordHash,
                firstName,
                lastName,
                role,
                true,
                now,
                now
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
