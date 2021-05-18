package com.example.demo.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Data;

@Data
public class RoleDTO {
    
    @Enumerated(EnumType.STRING)
    private Role role;

}
