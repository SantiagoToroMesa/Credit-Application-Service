package com.springboot.credit_application_service.infrastructure.security.repository;

import com.springboot.credit_application_service.infrastructure.security.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);
}