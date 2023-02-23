package com.ind.tr.persistance.model;

import java.time.LocalDate;
import java.util.UUID;

public class PortfolioEntity {
    private UUID id;
    private UUID ownerId;
    private String name;
    private LocalDate createdDate;
    private LocalDate updatedDate;

    public PortfolioEntity(UUID id, UUID ownerId, String name, LocalDate createdDate, LocalDate updatedDate) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }
}
