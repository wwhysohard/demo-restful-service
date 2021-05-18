package com.example.demo.security;

import java.util.Collection;
import java.util.List;

import com.example.demo.model.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSecurity implements UserDetails {

    private final String username;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;

    public static UserDetails getFromUser(User user) {
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), 
            user.getPassword(), 
            user.getRole().getAuthorities()
        );
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}