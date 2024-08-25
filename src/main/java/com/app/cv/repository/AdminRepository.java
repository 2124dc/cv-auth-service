package com.app.cv.repository;

import com.app.cv.model.AdminDto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AdminRepository extends MongoRepository<AdminDto, String> {
    Optional<AdminDto> findByEmail(String username);

    boolean existsByEmail(String email);
}

