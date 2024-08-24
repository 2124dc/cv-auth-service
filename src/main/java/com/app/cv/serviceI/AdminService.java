package com.app.cv.serviceI;

import com.app.cv.exception.NotFountException;
import com.app.cv.model.Admin;
import com.app.cv.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AdminService  {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    private AdminRepository userRepository;

    public UserDetails loadUserByUsername(String email) throws NotFountException {
        Admin admin = adminRepository.findByEmail(email);
        if (admin == null) {
            throw new NotFountException("User not found with email: " + email);
        }
        return new org.springframework.security.core.userdetails.User(admin.getEmail(), admin.getPassword(), new ArrayList<>());
    }
}
