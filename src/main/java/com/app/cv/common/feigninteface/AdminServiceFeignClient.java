package com.app.cv.common.feigninteface;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.app.cv.model.AdminRegisterRequest;
import com.app.cv.model.SuccessResponse;

@FeignClient(name = "admin-microservice", url = "${ADMIN_SERVICE_URL}")
public interface AdminServiceFeignClient {
    @PostMapping("/cv-admin/admin/register")
    SuccessResponse registerAdmin(@RequestBody AdminRegisterRequest adminRegisterRequest);
}
