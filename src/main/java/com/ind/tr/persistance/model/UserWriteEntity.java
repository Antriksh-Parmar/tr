package com.ind.tr.persistance.model;

import java.util.Optional;
import java.util.UUID;

public class UserWriteEntity {
    private UUID id;
    private Optional<String> firstName;
    private Optional<String> lastName;
    private Optional<String> email;
    private Optional<String> passwordHash;

    public UserWriteEntity(UUID id, Optional<String> firstName, Optional<String> lastName, Optional<String> email, Optional<String> passwordHash) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Optional<String> getFirstName() {
        return firstName;
    }

    public void setFirstName(Optional<String> firstName) {
        this.firstName = firstName;
    }

    public Optional<String> getLastName() {
        return lastName;
    }

    public void setLastName(Optional<String> lastName) {
        this.lastName = lastName;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public void setEmail(Optional<String> email) {
        this.email = email;
    }

    public Optional<String> getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(Optional<String> passwordHash) {
        this.passwordHash = passwordHash;
    }
}
