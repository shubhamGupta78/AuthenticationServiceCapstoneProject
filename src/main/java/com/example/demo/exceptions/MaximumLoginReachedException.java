package com.example.demo.exceptions;

public class MaximumLoginReachedException extends RuntimeException {
    public MaximumLoginReachedException(String message) {
        super(message);
    }
}
