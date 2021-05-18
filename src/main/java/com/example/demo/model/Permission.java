package com.example.demo.model;

public enum Permission {
    
    read("user:read"),
    write("user:write");

    private final String permission;

    private Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

}
