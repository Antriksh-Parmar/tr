package com.ind.tr.service.model;

import java.time.LocalDate;
import java.util.UUID;

public class Portfolio {
    private UUID id;
    private String name;
    private UUID owner;
    private LocalDate createdDate;
    private LocalDate updatedDate;

    public Portfolio(UUID id, String name, UUID owner, LocalDate createdDate, LocalDate updatedDate) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }
}
