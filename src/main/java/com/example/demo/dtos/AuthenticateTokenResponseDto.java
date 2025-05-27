package com.example.demo.dtos;

import lombok.Data;

@Data
public class AuthenticateTokenResponseDto {
    private AuthenticationTokenStatus authenticationStatus;
}
