package com.ind.tr.service;

import com.ind.tr.controller.auth.JwtTokenService;
import com.ind.tr.repository.UserDao;
import com.ind.tr.service.model.GuestUser;
import com.ind.tr.service.translator.UserTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationServiceImpl implements RegistrationService {

    private final Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    @Autowired
    private UserTranslator userTranslator;

    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Override
    public String createGuestUser() {
        UUID uuid = UUID.randomUUID();
        GuestUser guestUser = new GuestUser(uuid);
        userDao.saveGuestUser(userTranslator.toUserWriteEntity(guestUser));
        return jwtTokenService.generateJWTToken(uuid);
    }
}
