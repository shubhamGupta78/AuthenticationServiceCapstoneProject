package com.example.demo.exceptions;

public class InvalidLogoutRequest extends RuntimeException {
    public InvalidLogoutRequest(String message) {
        super(message);

    }
}
