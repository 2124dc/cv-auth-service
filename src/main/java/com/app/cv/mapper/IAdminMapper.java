package com.app.cv.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import com.app.cv.model.Admin;
import com.app.cv.model.AuthRegisterRequest;

@Mapper(componentModel = "spring")
@Component
public interface IAdminMapper {

    @Mapping(source = "username", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "mobile", target = "mobile")
    @Mapping(source = "name", target = "name")
    Admin mapAdminData(AuthRegisterRequest authRegisterRequest);
   
}
