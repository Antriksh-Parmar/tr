package com.ind.tr.service.translator;

import com.ind.tr.controller.model.PortfolioResponse;
import com.ind.tr.persistance.model.PortfolioEntity;
import com.ind.tr.service.model.Portfolio;

public interface PortfolioTranslator {
    PortfolioEntity toPortfolioEntity(Portfolio portfolio);
    PortfolioResponse toPortfolioResponse(Portfolio portfolio);
}
