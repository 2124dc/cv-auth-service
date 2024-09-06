package com.app.cv.repository;

import com.app.cv.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AdminRepository extends MongoRepository<Admin, String> {
    Optional<Admin> findByEmail(String username);
    boolean existsByEmail(String email);
}

