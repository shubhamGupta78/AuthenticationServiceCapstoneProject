package com.example.demo.dtos;

import com.example.demo.models.User;
import lombok.Data;

@Data
public class RegisterUserResponseDto {

    private int id;
    private String username;
    private String email;
    private RequestStatus requestStatus;

    public static RegisterUserResponseDto toDto(User user) {

        RegisterUserResponseDto dto = new RegisterUserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;

    }
}
