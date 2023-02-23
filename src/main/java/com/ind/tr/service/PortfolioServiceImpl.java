package com.ind.tr.service;

import com.ind.tr.controller.model.PortfolioResponse;
import com.ind.tr.persistance.PortfolioDao;
import com.ind.tr.persistance.UserDao;
import com.ind.tr.service.model.PlatformUser;
import com.ind.tr.service.model.Portfolio;
import com.ind.tr.service.model.User;
import com.ind.tr.service.translator.PortfolioTranslator;
import com.ind.tr.service.translator.UserTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

    @Override
    public PortfolioResponse createPortfolio(String userId) {
        UUID userUuid = UUID.fromString(userId);
        User user = userTranslator.fromUserReadEntity(userDao.getUser(userUuid));
        Portfolio portfolio = generatePortfolioDetails(user);
        portfolioDao.savePortfolio(portfolioTranslator.toPortfolioEntity(portfolio));
        return portfolioTranslator.toPortfolioResponse(portfolio);
    }

    private Portfolio generatePortfolioDetails(User user) {
        LocalDate today = istClock.getTodayDate();
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
            return platformUser.getFirstName() + "'s portfolio: " + istClock.getTodayDate().toString();
        } else {
            return "My portfolio: " + istClock.getTodayDate().toString();
        }
    }
}
