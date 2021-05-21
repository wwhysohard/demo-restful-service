package com.example.demo.security;

public interface TokenProvider {
    
    public String getToken(String username, String role);

}
