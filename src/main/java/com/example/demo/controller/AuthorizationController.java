package com.example.demo.controller;

import com.example.demo.model.AuthorizationRequestDTO;
import com.example.demo.service.AuthorizationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @Autowired
    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthorizationRequestDTO request) {
        return authorizationService.authenticate(request);
    }

}