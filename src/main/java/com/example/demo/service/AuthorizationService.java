package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.model.AuthorizationRequestDTO;
import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;
import com.example.demo.security.JwtProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    
    private final AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private JwtProvider jwtProvider;

    @Autowired
    public AuthorizationService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtProvider = jwtTokenProvider;
    }

    public ResponseEntity<?> authenticate(AuthorizationRequestDTO request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            Map<Object, Object> response = new HashMap<>();
            response.put("username", request.getUsername());
            response.put("token", getToken(getUser(request)));
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.FORBIDDEN);
        }
    }

    public User getUser(AuthorizationRequestDTO request) {
        return userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
    }

    public String getToken(User user) {
        return jwtProvider.getToken(user.getUsername(), user.getRole().name());
    }

    public void unauthenticate(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

}
