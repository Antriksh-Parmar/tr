package com.ind.tr.service;

import com.ind.tr.controller.model.PortfolioResponse;
import com.ind.tr.service.model.User;

import java.util.List;
import java.util.UUID;

public interface PortfolioService {
    PortfolioResponse createPortfolio(User user);

    List<PortfolioResponse> getPortfolios(User user);

    PortfolioResponse getPortfolio(UUID portfolioId);

    void deletePortfolio(UUID portfolioId);

    void deletePortfolios(User user);
}
