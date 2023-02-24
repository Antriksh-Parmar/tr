package com.ind.tr.controller;

import com.ind.tr.controller.model.BaseResponse;
import com.ind.tr.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping(value = "/guest")
    public ResponseEntity<BaseResponse> createGuestUser() {
        String guestToken = registrationService.createGuestUser();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + guestToken);
        return new ResponseEntity<>(null, headers, HttpStatus.OK);
    }
}
