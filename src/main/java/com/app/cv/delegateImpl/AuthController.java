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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.cv.api.AuthApiDelegate;
import com.app.cv.common.classes.Common;
import com.app.cv.common.feigninteface.AdminServiceFeignClient;
import com.app.cv.exception.InvalidUserException;
import com.app.cv.model.Admin;
import com.app.cv.model.AdminCreds;
import com.app.cv.model.AuthRegisterRequest;
import com.app.cv.model.AuthRequest;
import com.app.cv.model.AuthResponse;
import com.app.cv.model.SuccessResponse;
import com.app.cv.service.AuthDetailsService;
import com.app.cv.service.util.JwtUtil;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

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

    @Autowired
    AdminServiceFeignClient adminServiceFeignClient;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    // JWT validation endpoint
    @PostMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestBody String token) {
        boolean isValid = jwtUtil.validateToken(token);
        return ResponseEntity.ok(isValid);
    }

    @Override
    public ResponseEntity<SuccessResponse> authLoginPost(@Valid AuthRequest authRequest) {
        logger.info("AuthController -> authLoginPost : {}", authRequest);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            logger.error("Invalid Username or password ... : {}", authRequest);
            throw new InvalidUserException("Invalid Username or password ...");
        }

        final UserDetails userDetails = authDetailsService.loadUserByUsername(authRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails);
        return new ResponseEntity<>(Common.getSuccessResponse("Operation Successfull", token), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SuccessResponse> authRegisterPost(AuthRegisterRequest authRegisterRequest) {
        logger.info("AuthController -> authRegisterPost : {}", authRegisterRequest);
        AdminCreds adminCreds = authDetailsService.saveAdmin(authRegisterRequest, passwordEncoder);
        if (adminCreds.getId() != null) {
            return new ResponseEntity<>(Common.getSuccessResponse("Operation Successfull", adminServiceFeignClient.registerOwner(authRegisterRequest).getData()), HttpStatus.OK);
        }   
        throw new InvalidUserException("Something went wrong....");
    }
}