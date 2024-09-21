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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.cv.api.AdminApiDelegate;
import com.app.cv.common.classes.Common;
import com.app.cv.common.feigninteface.AdminServiceFeignClient;
import com.app.cv.exception.InvalidUserException;
import com.app.cv.model.AdminCreds;
import com.app.cv.model.AdminRegisterRequest;
import com.app.cv.model.SuccessResponse;
import com.app.cv.service.AdminService;
import com.app.cv.service.util.JwtUtil;


@RestController
public class AdminController implements AdminApiDelegate{

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AdminService authDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AdminServiceFeignClient adminServiceFeignClient;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Override
    public ResponseEntity<SuccessResponse> registerAdmin(AdminRegisterRequest adminRegisterRequest) {
        logger.info("AuthController -> registerAdmin : {}", adminRegisterRequest);
        AdminCreds adminCreds = authDetailsService.saveAdmin(adminRegisterRequest, passwordEncoder);
        if (adminCreds.getId() != null) {
            return new ResponseEntity<>(Common.getSuccessResponse("Operation Successfull", adminServiceFeignClient.registerAdmin(adminRegisterRequest).getData()), HttpStatus.OK);
        }   
        throw new InvalidUserException("Something went wrong....");
    }
}

