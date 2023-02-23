package com.ind.tr.service.model;

import java.util.UUID;

public abstract class User {
    protected UUID id;

    public User(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
