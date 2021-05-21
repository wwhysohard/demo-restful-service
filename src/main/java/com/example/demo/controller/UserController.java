package com.example.demo.controller;

import java.util.List;

import com.example.demo.model.User;
import com.example.demo.model.RoleDTO;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:read')")
    public User findById(@PathVariable("id") Integer id) {
        return userService.findById(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('user:modify')")
    public User updateUser(@PathVariable("id") Integer id, @RequestBody RoleDTO newUserRole) {
        return userService.updateUserRole(id, newUserRole);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:modify')")
    public void deleteUser(@PathVariable("id") Integer id) {
        userService.deleteById(id);
    }
    
}
