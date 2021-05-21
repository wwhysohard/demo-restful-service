package com.example.demo.security;

import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider implements TokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expirationTime;

    @PostConstruct
    private void initialize() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
    
    @Override
    public String getToken(String username, String role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        return createToken(claims);
    }

    private String createToken(Claims claims) {
        return Jwts.builder()
                   .setClaims(claims)
                   .setIssuedAt(getIssuedDate())
                   .setExpiration(getExpirationDate())
                   .signWith(SignatureAlgorithm.HS256, secretKey)
                   .compact();
    }

    private Date getIssuedDate() {
        return new Date();
    }

    private Date getExpirationDate() {
        return new Date(getIssuedDate().getTime() + expirationTime);
    }

}