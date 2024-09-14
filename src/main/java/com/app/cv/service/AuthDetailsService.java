package com.app.cv.service;

import com.app.cv.delegateImpl.AuthController;
import com.app.cv.exception.UserAlreadyExistException;
import com.app.cv.mapper.IAdminMapper;
// import com.app.cv.mapper.IAdminMapper;
import com.app.cv.model.Admin;
import com.app.cv.model.AuthRegisterRequest;
import com.app.cv.repository.AdminRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AuthDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    IAdminMapper mapper;

    // Create a logger instance for the class
    private static final Logger logger = LoggerFactory.getLogger(AuthDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("AuthDetailsService -> loadUserByUsername : {}", email);
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isEmpty()) {
            logger.error("AuthDetailsService -> saveAdmin -> isEmpty: {}", admin.isEmpty());
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(admin.get().getEmail(), admin.get().getPassword(), new ArrayList<>());
    }

    public Admin saveAdmin(AuthRegisterRequest authRegisterRequest, PasswordEncoder passwordEncoder) {
        logger.info("AuthDetailsService -> saveAdmin : {}", authRegisterRequest);
        if (adminRepository.existsByEmail(authRegisterRequest.getUsername())) {
            logger.error("AuthDetailsService -> saveAdmin -> existsByEmail: {}", authRegisterRequest.getUsername());
            throw new UserAlreadyExistException("Email already exists. Please choose a different email.");
        }
        Admin admin = mapper.mapAdminData(authRegisterRequest);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin = adminRepository.save(admin);
        return admin;
    }
}
