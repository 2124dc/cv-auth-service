package com.app.cv.service;

import com.app.cv.common.feigninteface.OwnerServiceFeignClient;
import com.app.cv.exception.UserAlreadyExistException;
import com.app.cv.mapper.IOwnerMapper;
import com.app.cv.model.Owner;
import com.app.cv.model.OwnerCreds;
import com.app.cv.model.OwnerRegisterRequest;
import com.app.cv.model.SuccessResponse;
import com.app.cv.repository.OwnerCredsRepository;
import com.app.cv.repository.OwnerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class OwnerService implements UserDetailsService {

    @Autowired
    private OwnerRepository adminRepository;

    @Autowired
    private OwnerCredsRepository adminCredsRepository;

    @Autowired
    IOwnerMapper mapper;

    // Create a logger instance for the class
    private static final Logger logger = LoggerFactory.getLogger(OwnerService.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("AuthDetailsService -> loadUserByUsername : {}", email);
        Optional<OwnerCreds> admin = adminCredsRepository.findByEmail(email);
        if (admin.isEmpty()) {
            logger.error("AuthDetailsService -> saveOwner -> isEmpty: {}", admin.isEmpty());
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(admin.get().getEmail(), admin.get().getPassword(), new ArrayList<>());
    }

    public OwnerCreds saveOwner(OwnerRegisterRequest adminRegisterRequest, PasswordEncoder  passwordEncoder) {
        logger.info("AuthDetailsService -> saveOwner : {}", adminRegisterRequest);
        if (adminCredsRepository.existsByEmail(adminRegisterRequest.getUsername())) {
            logger.error("AuthDetailsService -> saveOwner -> existsByEmail: {}", adminRegisterRequest.getUsername());
            throw new UserAlreadyExistException("Email already exists. Please choose a different email.");
        }

        OwnerCreds adminCred = mapper.mapOwnerCredsData(adminRegisterRequest);
        adminCred.setPassword(passwordEncoder.encode(adminCred.getPassword()));
        adminCred = adminCredsRepository.save(adminCred);
        logger.info("Credentials saved Successfully...for  : {}", adminCred.getEmail());
        return adminCred;
    }
}
