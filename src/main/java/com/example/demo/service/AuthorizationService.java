package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;

import com.example.demo.model.AuthorizationRequestDTO;
import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;
import com.example.demo.security.TokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    
    private final AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private TokenProvider tokenProvider;

    @Autowired
    public AuthorizationService(AuthenticationManager authenticationManager, UserRepository userRepository, TokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    public ResponseEntity<?> authenticate(AuthorizationRequestDTO request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            return ResponseEntity.ok(getResponse(request));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.FORBIDDEN);
        }
    }

    private Map<Object, Object> getResponse(AuthorizationRequestDTO request) {
        Map<Object, Object> response = new HashMap<>();
        response.put("username", request.getUsername());
        response.put("token", getToken(getUser(request)));
        return response;
    }

    private User getUser(AuthorizationRequestDTO request) {
        return userRepository.findByUsername(request.getUsername())
                             .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
    }

    private String getToken(User user) {
        return tokenProvider.getToken(user.getUsername(), user.getRole().name());
    }

}
