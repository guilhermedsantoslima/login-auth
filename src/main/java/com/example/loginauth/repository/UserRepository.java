package com.example.loginauth.repository;

import com.example.loginauth.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

//    String findByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
}
