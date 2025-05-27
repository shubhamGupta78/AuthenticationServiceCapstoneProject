package com.example.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Role extends BaseModel {

    private String role;

    @OneToMany(mappedBy="role")
    private List<UserRole> userRoleList;
}
