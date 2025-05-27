package com.example.demo.controllers;

import com.example.demo.dtos.*;
import com.example.demo.models.User;
import com.example.demo.services.AuthService;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class AuthControllers {

    private final AuthService authService;

    public AuthControllers(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign_up")
    public ResponseEntity<RegisterUserResponseDto> registerUser(@RequestBody RegisterUserRequestDto registerUserRequestDto) {

        RegisterUserResponseDto registerUserResponseDto=new RegisterUserResponseDto();

        try {
            User user = authService.CreateUser(registerUserRequestDto.toUser());
            registerUserResponseDto = RegisterUserResponseDto.toDto(user);
            registerUserResponseDto.setRequestStatus(RequestStatus.SUCCESS);
        }
        catch (Exception e) {
           registerUserResponseDto.setRequestStatus(RequestStatus.FAILURE);
        }

        return ResponseEntity.ok(registerUserResponseDto);


    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticateUserResponseDto> authenticateUser(@RequestBody AuthenticateUserRequestDto authenticateUserRequestDto)
    {
        AuthenticateUserResponseDto authenticateUserResponseDto=new AuthenticateUserResponseDto();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

        try {
            System.out.println(authenticateUserRequestDto.getEmail());
            String token = authService.Login(authenticateUserRequestDto.toUser());
            headers.add("token", token);
            authenticateUserResponseDto.setStatus(RequestStatus.SUCCESS);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            authenticateUserResponseDto.setStatus(RequestStatus.FAILURE);
        }

        ResponseEntity<AuthenticateUserResponseDto> responseEntity=new ResponseEntity<>(authenticateUserResponseDto, headers, HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/authenticate")
    public ResponseEntity<AuthenticateTokenResponseDto> validateToken(@RequestParam("token") String token)
    {
        AuthenticateTokenResponseDto authenticateTokenResponseDto=new AuthenticateTokenResponseDto();
        try {
            Boolean result = authService.validateJwtToken(token);
            authenticateTokenResponseDto.setAuthenticationStatus(result==true?AuthenticationTokenStatus.SUCCESS:AuthenticationTokenStatus.FAILED);
        }
        catch (Exception e) {
            authenticateTokenResponseDto.setAuthenticationStatus(AuthenticationTokenStatus.FAILED);
        }

        return ResponseEntity.ok(authenticateTokenResponseDto);

    }

    @GetMapping("/logout")
    public ResponseEntity<LogoutResponseDto> LogOut(@RequestHeader("token") String token)
    {
        LogoutResponseDto logoutResponseDto=new LogoutResponseDto();
        try {
            authService.logout(token);
            logoutResponseDto.setStatus(LogoutStatus.LogoutSuccess);
        }
        catch (Exception e) {
            logoutResponseDto.setStatus(LogoutStatus.LogoutFail);

        }
        return ResponseEntity.ok(logoutResponseDto);
    }
}
