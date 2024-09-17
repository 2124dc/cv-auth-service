package com.app.cv.common.feigninteface;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.app.cv.model.OwnerRegisterRequest;
import com.app.cv.model.SuccessResponse;

@FeignClient(name = "owner-microservice", url = "${OWNER_SERVICE_URL}")
public interface OwnerServiceFeignClient {
    @PostMapping("/cv-owner/owner/register")
    SuccessResponse registerOwner(@RequestBody OwnerRegisterRequest ownerRegisterRequest);
}
