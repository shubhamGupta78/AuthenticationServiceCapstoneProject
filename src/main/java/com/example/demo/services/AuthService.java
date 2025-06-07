package com.example.demo.services;

import com.example.demo.exceptions.InvalidLogoutRequest;
import com.example.demo.exceptions.UserAlreadyExist;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    public User CreateUser(User user) throws UserAlreadyExist, JsonProcessingException;
    public String Login(User user) throws  UserNotFoundException;
    public Boolean validateJwtToken(String token) throws InvalidLogoutRequest;

    public Boolean logout(String token);
}
