package com.example.demo.security;

import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {

    @Value("${jwt.header}")
    private String authorizationHeader;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expirationTime;

    private final UserDetailsService userDetailsService;

    public JwtProvider(@Qualifier("UserService") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    public void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
    
    public String getToken(String username, String role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        return createToken(claims);
    }

    public String createToken(Claims claims) {
        return Jwts.builder()
                   .setClaims(claims)
                   .setIssuedAt(getIssuedDate())
                   .setExpiration(getExpirationDate())
                   .signWith(SignatureAlgorithm.HS256, secretKey)
                   .compact();
    }

    public Date getIssuedDate() {
        return new Date();
    }

    public Date getExpirationDate() {
        return new Date(getIssuedDate().getTime() + expirationTime);
    }

    public boolean isValid(String token) {
        try {
            return !extractExpiration(token).before(new Date());
        } catch (IllegalArgumentException | JwtException e) {
            throw new JwtAuthenticationException("Invalid or expired token, access denied!", HttpStatus.UNAUTHORIZED); 
        }
    }

    public Date extractExpiration(String token) {
        return extractClaims(token).getBody().getExpiration();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(extractUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String  extractUsername(String token) {
        return extractClaims(token).getBody().getSubject();
    }

    public Jws<Claims> extractClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(authorizationHeader);
    }

}