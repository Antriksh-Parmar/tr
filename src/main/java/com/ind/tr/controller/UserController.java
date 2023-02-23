package com.ind.tr.controller;

import com.ind.tr.controller.model.UserRequest;
import com.ind.tr.controller.model.UserResponse;
import com.ind.tr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/new")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {

        return null;
    }

    @PostMapping("/guest")
    public ResponseEntity<UserResponse> createGuestUser() {
        UserResponse response = userService.createGuestUser();
        return ResponseEntity.ok(response);
    }
}
