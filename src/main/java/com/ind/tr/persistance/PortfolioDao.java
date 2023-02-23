package com.ind.tr.persistance;

import com.ind.tr.persistance.model.PortfolioEntity;

public interface PortfolioDao {
    void savePortfolio(PortfolioEntity portfolioEntity);
}
