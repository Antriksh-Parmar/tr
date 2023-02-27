package com.ind.tr.repository;

import com.ind.tr.repository.model.PortfolioEntity;

import java.util.List;
import java.util.UUID;

public interface PortfolioDao {
    void savePortfolio(PortfolioEntity portfolioEntity);

    PortfolioEntity getPortfolio(UUID portfolioId);

    List<PortfolioEntity> getAllPortfolios(UUID userId);

    void deletePortfolio(UUID portfolioId);

    void deletePortfolios(UUID uuid);
}
