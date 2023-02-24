package com.ind.tr.service.translator;

import com.ind.tr.controller.model.PortfolioResponse;
import com.ind.tr.repository.model.PortfolioEntity;
import com.ind.tr.service.model.Portfolio;
import org.springframework.stereotype.Component;

@Component
public class PortfolioTranslatorImpl implements PortfolioTranslator {

    @Override
    public PortfolioEntity toPortfolioEntity(Portfolio portfolio) {
        return new PortfolioEntity(
                portfolio.getId(),
                portfolio.getOwner(),
                portfolio.getName(),
                portfolio.getCreatedDate(),
                portfolio.getUpdatedDate()
        );
    }

    @Override
    public PortfolioResponse toPortfolioResponse(Portfolio portfolio) {
        return new PortfolioResponse(portfolio.getId(), portfolio.getName());
    }
}
