package com.ind.tr.service;

import com.ind.tr.controller.model.UserResponse;
import com.ind.tr.persistance.UserDao;
import com.ind.tr.service.model.GuestUser;
import com.ind.tr.service.translator.UserTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserTranslator userTranslator;

    @Autowired
    private UserDao userDao;

    @Override
    public UserResponse createGuestUser() {
        GuestUser guestUser = new GuestUser(UUID.randomUUID());
        userDao.saveGuestUser(userTranslator.toUserWriteEntity(guestUser));
        return userTranslator.toGuestUserResponse(guestUser);
    }

    @Override
    public UserResponse createUser() {
        return null;
    }
}
