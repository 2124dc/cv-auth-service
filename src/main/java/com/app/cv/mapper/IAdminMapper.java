package com.app.cv.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Component;

import com.app.cv.common.classes.UserRole;
import com.app.cv.model.Admin;
import com.app.cv.model.AdminCreds;
import com.app.cv.model.AuthRegisterRequest;

@Mapper(componentModel = "spring")
@Component
public interface IAdminMapper {

    @Mapping(source = "username", target = "email")
    @Mapping(source = "password", target = "password")
    AdminCreds mapAdminCredsData(AuthRegisterRequest authRegisterRequest);

    @Mapping(source = "username", target = "email")
    @Mapping(source = "mobile", target = "mobile")
    @Mapping(source = "userRole", target = "userRole", qualifiedByName = "mapUserRoleEnum")
    Admin mapAdminData(AuthRegisterRequest authRegisterRequest);

    @Named("mapUserRoleEnum")
    default UserRole mapUserRoleEnum(AuthRegisterRequest.UserRoleEnum userRoleEnum) {
        if (userRoleEnum == null) {
            return null;
        }
        switch (userRoleEnum) {
            case ADMIN:
                return UserRole.ADMIN;
            case OWNER:
                return UserRole.OWNER;
            default:
                throw new IllegalArgumentException("Unknown enum value: " + userRoleEnum);
        }
    }
}
