package com.ind.tr.repository.model;

import java.sql.Date;
import java.util.UUID;

public class PortfolioEntity {
    private UUID id;
    private UUID ownerId;
    private String name;
    private Date createdDate;
    private Date updatedDate;

    public PortfolioEntity(UUID id, UUID ownerId, String name, Date createdDate, Date updatedDate) {
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }
}
