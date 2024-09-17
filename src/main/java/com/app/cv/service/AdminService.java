package com.app.cv.service;

import com.app.cv.common.feigninteface.AdminServiceFeignClient;
import com.app.cv.exception.UserAlreadyExistException;
import com.app.cv.mapper.IAdminMapper;
import com.app.cv.model.Admin;
import com.app.cv.model.AdminCreds;
import com.app.cv.model.AdminRegisterRequest;
import com.app.cv.model.SuccessResponse;
import com.app.cv.repository.AdminCredsRepository;
import com.app.cv.repository.AdminRepository;

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
public class AdminService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminCredsRepository adminCredsRepository;

    @Autowired
    IAdminMapper mapper;

    // Create a logger instance for the class
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("AuthDetailsService -> loadUserByUsername : {}", email);
        Optional<AdminCreds> admin = adminCredsRepository.findByEmail(email);
        if (admin.isEmpty()) {
            logger.error("AuthDetailsService -> saveAdmin -> isEmpty: {}", admin.isEmpty());
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(admin.get().getEmail(), admin.get().getPassword(), new ArrayList<>());
    }

    public AdminCreds saveAdmin(AdminRegisterRequest adminRegisterRequest, PasswordEncoder passwordEncoder) {
        logger.info("AuthDetailsService -> saveAdmin : {}", adminRegisterRequest);
        if (adminCredsRepository.existsByEmail(adminRegisterRequest.getUsername())) {
            logger.error("AuthDetailsService -> saveAdmin -> existsByEmail: {}", adminRegisterRequest.getUsername());
            throw new UserAlreadyExistException("Email already exists. Please choose a different email.");
        }

        AdminCreds adminCred = mapper.mapAdminCredsData(adminRegisterRequest);
        adminCred.setPassword(passwordEncoder.encode(adminCred.getPassword()));
        adminCred = adminCredsRepository.save(adminCred);
        logger.info("Credentials saved Successfully...for  : {}", adminCred.getEmail());
        return adminCred;
    }
}
