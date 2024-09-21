package com.app.cv.repository;

import com.app.cv.model.AdminCreds;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AdminCredsRepository extends MongoRepository<AdminCreds, String> {
    Optional<AdminCreds> findByEmail(String username);
    boolean existsByEmail(String email);
}

