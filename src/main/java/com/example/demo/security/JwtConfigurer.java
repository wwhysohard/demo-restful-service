package com.example.demo.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    
    private final JwtFilter jwtFilter;

    public JwtConfigurer(JwtFilter jwtTokenFilter) {
        this.jwtFilter = jwtTokenFilter;
    }

    @Override
    public void configure(HttpSecurity httpSecurity) {
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
