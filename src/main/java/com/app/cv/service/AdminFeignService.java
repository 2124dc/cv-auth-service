package com.app.cv.service;

import com.app.cv.common.feigninteface.AdminServiceFeignClient;
import com.app.cv.model.AuthRegisterRequest;
import com.app.cv.model.SuccessResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminFeignService  {
    private final AdminServiceFeignClient adminServiceFeignClient;

    @Autowired
    public AdminFeignService(AdminServiceFeignClient adminServiceFeignClient) {
        this.adminServiceFeignClient = adminServiceFeignClient;
    }
    private static final Logger logger = LoggerFactory.getLogger(AuthDetailsService.class);

    public SuccessResponse saveAdmin(AuthRegisterRequest authRegisterRequest) {
        logger.info("AuthDetailsService -> saveAdmin : {}", authRegisterRequest.getUsername());
        return adminServiceFeignClient.registerOwner(authRegisterRequest);
    }

}
