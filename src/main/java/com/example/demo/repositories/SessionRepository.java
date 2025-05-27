package com.example.demo.repositories;

import com.example.demo.models.Session;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session,Integer> {


    Integer countByUser(User user);
    Optional<Session> findByToken(String token);
}
