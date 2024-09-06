package com.app.cv.service;

import com.app.cv.exception.UserAlreadyExistException;
import com.app.cv.mapper.IAdminMapper;
// import com.app.cv.mapper.IAdminMapper;
import com.app.cv.model.Admin;
import com.app.cv.model.AuthRegisterRequest;
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

    @Autowired
    IAdminMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(admin.get().getEmail(), admin.get().getPassword(), new ArrayList<>());
    }

    public void saveAdmin(AuthRegisterRequest authRegisterRequest, PasswordEncoder passwordEncoder) {
        Admin admin = mapper.mapAdminData(authRegisterRequest);
        System.out.println("Admin : ---->> "+admin.toString());
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin = adminRepository.save(admin);
    }
}
