package com.example.demo.dtos;

import lombok.Data;
@Data
public class EmailDto {

    private String from;
    private String to;
    private String subject;
    private String body;
}
