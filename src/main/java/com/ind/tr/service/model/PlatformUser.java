package com.ind.tr.service.model;

import java.util.UUID;

public class PlatformUser extends User {
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;

    public PlatformUser(UUID id, String firstName, String lastName, String email, String passwordHash) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public boolean isGuestUser() {
        return false;
    }
}
