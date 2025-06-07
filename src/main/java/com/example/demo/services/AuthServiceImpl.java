package com.example.demo.services;

import com.example.demo.Client.KafkaClient;
import com.example.demo.configs.JwtConfigs;
import com.example.demo.dtos.EmailDto;
import com.example.demo.exceptions.InvalidLogoutRequest;
import com.example.demo.exceptions.MaximumLoginReachedException;
import com.example.demo.exceptions.UserAlreadyExist;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.models.Session;
import com.example.demo.models.SessionStatus;
import com.example.demo.models.User;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.SessionRepository;
import com.example.demo.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {

    private ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SessionRepository sessionRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private JwtConfigs jwtConfigs;
    private KafkaClient kafkaClient;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, SessionRepository sessionRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtConfigs jwtConfigs, KafkaClient kafkaClient, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtConfigs = jwtConfigs;
        this.kafkaClient = kafkaClient;
        this.objectMapper = objectMapper;

    }

    @Override
    public User CreateUser(User user) throws UserAlreadyExist, JsonProcessingException {

        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExist("user with the given email already exist");
        }
        String hashedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        EmailDto emailDto = new EmailDto();
        emailDto.setFrom("shubhamgupta746690@gmail.com");
        emailDto.setTo(user.getEmail());
        emailDto.setSubject("User Registration successful");
        emailDto.setBody("welcome to our authenication service");



        kafkaClient.send("welcome_user",objectMapper.writeValueAsString(emailDto));
        return userRepository.save(user);

    }

    @Override
    public String Login(User user) throws UserNotFoundException {

        System.out.println(user.getEmail());
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("incorrect user credentials");

        }

        if(sessionRepository.countByUser(optionalUser.get()) > 2) {
            throw new MaximumLoginReachedException("maximum login limit reached");
        }

        User userDetails = optionalUser.get();

        if (!bCryptPasswordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
            throw new UserNotFoundException("incorrect user credentials");
        }

        return createJwtToken(userDetails);

    }

    @Override
    public Boolean validateJwtToken(String token) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(jwtConfigs.getSecretKey());

            SecretKey key = Keys.hmacShaKeyFor(decodedKey);

            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);  // parseClaimsJws for verifying signed tokens

            return true;
        } catch (JwtException e) {
            System.out.println("Invalid JWT: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean logout(String token) throws InvalidLogoutRequest {

        Optional<Session> optionalSession=sessionRepository.findByToken(token);
        if (optionalSession.isEmpty()) {
            throw new InvalidLogoutRequest("invalid request");
        }
        Session session=optionalSession.get();
        session.setSessionStatus(SessionStatus.EXPIRED);
        sessionRepository.save(session);
        return true;
     }


    private String createJwtToken(User user) {
        Map<String, Object> dataInJwt = new HashMap<>();
        dataInJwt.put("user_id", user.getId());
        dataInJwt.put("roles", user.getUserRoles());
        dataInJwt.put("email", user.getEmail());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30); // Expire in 30 days
        Date expirationDate = calendar.getTime();

        byte[] decodedKey = Base64.getDecoder().decode(jwtConfigs.getSecretKey());
        SecretKey key = Keys.hmacShaKeyFor(decodedKey);

        String token= Jwts.builder()
                .setClaims(dataInJwt)                      // custom data
                .setIssuedAt(new Date())                   // current time
                .setExpiration(expirationDate)             // 30 days later
                .signWith(SignatureAlgorithm.HS256,key) // use secure key
                .compact();

        Session session=new Session();
        session.setUser(user);
        session.setToken(token);
        session.setCreatedAt(Calendar.getInstance().getTime());
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setExpiryDate(expirationDate);

        sessionRepository.save(session);
        return token;
    }
}
