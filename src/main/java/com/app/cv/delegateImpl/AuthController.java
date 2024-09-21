package com.app.cv.delegateImpl;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.cv.api.AuthApiDelegate;
import com.app.cv.common.classes.Common;
import com.app.cv.exception.InvalidUserException;
import com.app.cv.model.AuthRequest;
import com.app.cv.model.SuccessResponse;
import com.app.cv.service.AdminService;
import com.app.cv.service.OwnerService;
import com.app.cv.service.util.JwtUtil;

@RestController
public class AuthController implements AuthApiDelegate {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AdminService adminService;

    @Autowired
    private OwnerService ownerService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestBody String token) {
        boolean isValid = jwtUtil.validateToken(token);
        return ResponseEntity.ok(isValid);
    }

    @Override
    public ResponseEntity<SuccessResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        logger.info("AuthController -> login : {}", authRequest);
        UserDetails userDetails = null;
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            logger.error("Invalid Username or password ... : {}", authRequest);
            throw new InvalidUserException("Invalid Username or password ...");
        }

        // Attempt to load user as Admin
        try {
            userDetails = adminService.loadUserByUsername(authRequest.getUsername());
        } catch (Exception e) {
            // If loading as Admin fails, try loading as Owner
            logger.info("Admin authentication failed, trying Owner authentication");
            try {
                userDetails = ownerService.loadUserByUsername(authRequest.getUsername());
            } catch (Exception ex) {
                logger.error("User not found in both Admin and Owner services");
                throw new InvalidUserException("User not found");
            }
        }

        final String token = jwtUtil.generateToken(userDetails);
        return new ResponseEntity<>(Common.getSuccessResponse("Operation Successful", token), HttpStatus.OK);
    }
}
