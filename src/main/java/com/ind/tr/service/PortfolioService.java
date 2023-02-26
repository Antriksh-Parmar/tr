package com.ind.tr.service;

import com.ind.tr.controller.model.PortfolioResponse;

import java.util.UUID;

public interface PortfolioService {
    PortfolioResponse createPortfolio(UUID userId);
}
