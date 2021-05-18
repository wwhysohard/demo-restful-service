package com.example.demo.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

import lombok.Getter;

@Getter
public class JwtAuthenticationException extends AuthenticationException {
    
    private HttpStatus httpStatus;

    public JwtAuthenticationException(String message) {
        super(message);
    }

    public JwtAuthenticationException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
