package com.example.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Data;
//import org.springframework.web.bind.support.SessionStatus;

import java.util.Date;

@Entity
@Data
public class Session extends BaseModel {

    private String token;
    private Date expiryDate;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.ORDINAL)
    private SessionStatus sessionStatus;

}
