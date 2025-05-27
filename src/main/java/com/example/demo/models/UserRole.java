package com.example.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class UserRole extends BaseModel {

    @ManyToOne
    private Role role;
    @ManyToOne
    private User user;
}
