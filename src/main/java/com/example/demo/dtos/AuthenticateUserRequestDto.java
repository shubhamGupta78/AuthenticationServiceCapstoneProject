package com.example.demo.dtos;

import com.example.demo.models.User;
import lombok.Data;

@Data
public class AuthenticateUserRequestDto {

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User toUser() {
        User user = new User();
        user.setEmail(email);
       user.setPassword(password);
        return user;
    }
}
