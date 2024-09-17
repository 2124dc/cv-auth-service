package com.app.cv.repository;

import com.app.cv.model.Owner;
import com.app.cv.model.OwnerCreds;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OwnerRepository extends MongoRepository<Owner, String> {
    Optional<OwnerCreds> findByEmail(String username);
    boolean existsByEmail(String email);
}

