package com.ind.tr.controller.model;

import java.util.UUID;

public class UserResponse {
    private UUID id;

    public UserResponse(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
