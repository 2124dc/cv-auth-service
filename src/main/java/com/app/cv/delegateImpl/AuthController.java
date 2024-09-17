
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
import org.springframework.web.bind.annotation.RestController;

import com.app.cv.api.AuthApiDelegate;
import com.app.cv.common.classes.Common;
import com.app.cv.exception.InvalidUserException;
import com.app.cv.model.AuthRequest;
import com.app.cv.model.SuccessResponse;
import com.app.cv.service.AdminService;
import com.app.cv.service.OwnerService;
import com.app.cv.service.util.JwtUtil;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class AuthController implements AuthApiDelegate{

     @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AdminService adminService;
    @Autowired
    private OwnerService ownerService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    // JWT validation endpoint
    @PostMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestBody String token) {
        boolean isValid = jwtUtil.validateToken(token);
        return ResponseEntity.ok(isValid);
    }

    @Override
    public ResponseEntity<SuccessResponse> login(@Valid AuthRequest authRequest) {
        logger.info("AuthController -> authLoginPost : {}", authRequest);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            logger.error("Invalid Username or password ... : {}", authRequest);
            throw new InvalidUserException("Invalid Username or password ...");
        }

        UserDetails userDetails = adminService.loadUserByUsername(authRequest.getUsername());
        System.out.println("********************************* : "+ userDetails.toString());
        if(userDetails.getPassword() == null || userDetails.getPassword().equals("")){
            userDetails = ownerService.loadUserByUsername(authRequest.getUsername());
        }
        final String token = jwtUtil.generateToken(userDetails);
        return new ResponseEntity<>(Common.getSuccessResponse("Operation Successfull", token), HttpStatus.OK);
    }
}