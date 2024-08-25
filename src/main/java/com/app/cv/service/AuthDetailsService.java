package com.app.cv.service;

import com.app.cv.exception.UserAlreadyExistException;
import com.app.cv.model.AdminDto;
import com.app.cv.repository.AdminRepository;
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AdminDto> admin = adminRepository.findByEmail(email);
        if (admin.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(admin.get().getEmail(), admin.get().getPassword(), new ArrayList<>());
    }

    public AdminDto saveAdmin(AdminDto adminDto, PasswordEncoder passwordEncoder) {
        if (adminRepository.existsByEmail(adminDto.getEmail())) {
            throw new UserAlreadyExistException("Email already exists. Please choose a different email.");
        }

        AdminDto admin = new AdminDto();
        admin.setName(adminDto.getName());
        admin.setEmail(adminDto.getEmail());
        admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        admin.setMobile(adminDto.getMobile());
        return adminRepository.save(admin);
    }
}
