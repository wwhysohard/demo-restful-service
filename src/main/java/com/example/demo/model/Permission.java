package com.example.demo.model;

public enum Permission {
    
    read("user:read"),
    modify("user:modify");

    private final String permission;

    private Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

}
