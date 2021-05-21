package com.example.demo.security;

import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Service
public class JwtValidator {

    @Value("${jwt.header}")
    private String authorizationHeader;

    @Value("${jwt.secret}")
    private String secretKey;
    
    private final UserDetailsService userDetailsService;

    public JwtValidator(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    private void initialize() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public boolean isValid(String token) {
        try {
            return !extractExpiration(token).before(new Date());
        } catch (IllegalArgumentException | JwtException e) {
            throw new JwtAuthenticationException("Invalid or expired token, access denied!", HttpStatus.UNAUTHORIZED); 
        }
    }

    private Date extractExpiration(String token) {
        return extractClaims(token).getBody().getExpiration();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(extractUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String extractUsername(String token) {
        return extractClaims(token).getBody().getSubject();
    }

    private Jws<Claims> extractClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(authorizationHeader);
    }

}
