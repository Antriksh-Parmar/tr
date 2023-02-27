package com.ind.tr.controller.model;

import com.ind.tr.controller.model.search.Status;

import java.util.UUID;

public class PortfolioResponse {
    private UUID id;
    private String name;

    public PortfolioResponse(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
