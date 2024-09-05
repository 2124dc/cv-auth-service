package com.app.cv.controller;

import com.app.cv.model.AdminDto;
import com.app.cv.service.AuthDetailsService;
import com.app.cv.service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthDetailsService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthDetailsService authDetailsService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody AdminDto authRequest) {
        System.out.println("Authenticating user: " + authRequest);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }

        final UserDetails userDetails = authDetailsService.loadUserByUsername(authRequest.getEmail());
        final String token = jwtUtil.generateToken(userDetails);

        return new ResponseEntity<>("Bearer "+token, HttpStatus.OK);
    }

    @PostMapping("/auth/register")
    public String registerUser(@RequestBody AdminDto adminDto) {
        System.out.println("Registering user: " + adminDto);
        authDetailsService.saveAdmin(adminDto, passwordEncoder);
        return "User registered successfully!";
    }

}