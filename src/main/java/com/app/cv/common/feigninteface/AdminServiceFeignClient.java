package com.app.cv.common.feigninteface;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.app.cv.model.AuthRegisterRequest;
import com.app.cv.model.SuccessResponse;

@FeignClient(name = "admin-microservice", url = "${OWNER_SERVICE_URL}")
public interface AdminServiceFeignClient {
    @PostMapping("/cv-admin/admin/register")
    SuccessResponse registerOwner(@RequestBody AuthRegisterRequest authRegisterRequest);
}
