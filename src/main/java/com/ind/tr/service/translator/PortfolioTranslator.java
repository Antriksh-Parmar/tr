package com.ind.tr.service.translator;

import com.ind.tr.controller.model.PortfolioResponse;
import com.ind.tr.controller.model.search.Status;
import com.ind.tr.repository.model.PortfolioEntity;
import com.ind.tr.service.model.Portfolio;

public interface PortfolioTranslator {
    PortfolioEntity toPortfolioEntity(Portfolio portfolio);

    PortfolioResponse toPortfolioResponse(Portfolio portfolio);

    PortfolioResponse toPortfolioResponse(PortfolioEntity portfolioEntity);

}
