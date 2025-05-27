package com.example.demo.dtos;

import com.example.demo.models.User;
import lombok.Data;

@Data
public class RegisterUserRequestDto {

    private String username;
    private String email;
    private String password;

    public User toUser() {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}
