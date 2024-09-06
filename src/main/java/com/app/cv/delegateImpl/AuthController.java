package com.app.cv.delegateImpl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import com.app.cv.api.AuthApiDelegate;
import com.app.cv.exception.InvalidUserException;
import com.app.cv.model.AuthRegisterRequest;
import com.app.cv.model.AuthRequest;
import com.app.cv.model.AuthResponse;
import com.app.cv.service.AuthDetailsService;
import com.app.cv.service.util.JwtUtil;

@RestController
public class AuthController implements AuthApiDelegate{

     @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthDetailsService authDetailsService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<AuthResponse> authLoginPost(@Valid AuthRequest authRequest) {
        System.out.println("Authenticating user: " + authRequest);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new InvalidUserException("Invalid Username or password ...");
        }

        final UserDetails userDetails = authDetailsService.loadUserByUsername(authRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails);
        // Create AuthResponse and set the token
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken("Bearer "+token);  // Assuming AuthResponse has a setter method for 'token'
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> authRegisterPost(AuthRegisterRequest authRegisterRequest) {
        System.out.println("AuthController -> authRegisterPost");
        authDetailsService.saveAdmin(authRegisterRequest, passwordEncoder);
        return new ResponseEntity<Void>(HttpStatus.OK);
        
    }


}