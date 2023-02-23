package com.ind.tr.service;

import com.ind.tr.controller.model.UserResponse;

public interface UserService {

    UserResponse createGuestUser();

    UserResponse createUser();

}
