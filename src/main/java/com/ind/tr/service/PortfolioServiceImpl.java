package com.ind.tr.service;

import com.ind.tr.controller.model.PortfolioResponse;
import com.ind.tr.repository.PortfolioDao;
import com.ind.tr.repository.UserDao;
import com.ind.tr.repository.model.PortfolioEntity;
import com.ind.tr.service.model.PlatformUser;
import com.ind.tr.service.model.Portfolio;
import com.ind.tr.service.model.User;
import com.ind.tr.service.translator.PortfolioTranslator;
import com.ind.tr.service.translator.UserTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PortfolioServiceImpl implements PortfolioService {

    @Autowired
    private PortfolioTranslator portfolioTranslator;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PortfolioDao portfolioDao;
    @Autowired
    private UserTranslator userTranslator;
    @Autowired
    private ISTClock istClock;

    
    // TODO Check roles and verify details
    @Override
    public PortfolioResponse createPortfolio(User user) {
        Portfolio portfolio = generatePortfolioDetails(user);
        portfolioDao.savePortfolio(portfolioTranslator.toPortfolioEntity(portfolio));
        return portfolioTranslator.toPortfolioResponse(portfolio);
    }

    @Override
    public List<PortfolioResponse> getPortfolios(User user) {
        List<PortfolioEntity> portfolioEntities = portfolioDao.getAllPortfolios(user.getId());
        return portfolioEntities.stream().map(portfolioTranslator::toPortfolioResponse).toList();
    }

    @Override
    public Optional<PortfolioResponse> getPortfolio(UUID portfolioId) {
        Optional<PortfolioEntity> portfolioEntity = portfolioDao.getPortfolio(portfolioId);
        return portfolioEntity.map(portfolioTranslator::toPortfolioResponse);
    }

    @Override
    public void deletePortfolio(UUID portfolioId) {
        portfolioDao.deletePortfolio(portfolioId);
    }

    @Override
    public void deletePortfolios(User user) {
        portfolioDao.deletePortfolios(user.getId());
    }

    private Portfolio generatePortfolioDetails(User user) {
        LocalDate today = istClock.getTodayLocalDate();
        return new Portfolio(
                UUID.randomUUID(),
                createPortfolioName(user),
                user.getId(),
                today,
                today
        );
    }

    private String createPortfolioName(User user) {
        if (user instanceof PlatformUser platformUser) {
            return platformUser.getFirstName() + "portfolio: " + istClock.getTodayLocalDate().toString();
        } else {
            return "My portfolio: " + istClock.getTodayLocalDate().toString();
        }
    }
}
