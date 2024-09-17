package com.app.cv.delegateImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.cv.api.OwnerApiDelegate;
import com.app.cv.common.classes.Common;
import com.app.cv.common.feigninteface.OwnerServiceFeignClient;
import com.app.cv.exception.InvalidUserException;
import com.app.cv.model.OwnerCreds;
import com.app.cv.model.OwnerRegisterRequest;
import com.app.cv.model.SuccessResponse;
import com.app.cv.service.OwnerService;
import com.app.cv.service.util.JwtUtil;

@RestController
public class OwnerController implements OwnerApiDelegate{

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    PasswordEncoder  passwordEncoder;

    @Autowired
    OwnerServiceFeignClient ownerServiceFeignClient;

    private static final Logger logger = LoggerFactory.getLogger(OwnerController.class);


    @Override
    public ResponseEntity<SuccessResponse> registerOwner(OwnerRegisterRequest ownerRegisterRequest) {
        logger.info("AuthController -> registerOwner : {}", ownerRegisterRequest);
        OwnerCreds adminCreds = ownerService.saveOwner(ownerRegisterRequest, passwordEncoder);
        if (adminCreds.getId() != null) {
            return new ResponseEntity<>(Common.getSuccessResponse("Operation Successfull", ownerServiceFeignClient.registerOwner(ownerRegisterRequest).getData()), HttpStatus.OK);
        }   
        throw new InvalidUserException("Something went wrong....");
    }
}

