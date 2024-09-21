package com.app.cv.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import com.app.cv.common.classes.UserRole;
import com.app.cv.model.Owner;
import com.app.cv.model.OwnerCreds;
import com.app.cv.model.OwnerRegisterRequest;
import com.app.cv.model.UserRoleEnum;

@Mapper(componentModel = "spring")
@Component
public interface IOwnerMapper {

    @Mapping(source = "username", target = "email")
    @Mapping(source = "password", target = "password")
    OwnerCreds mapOwnerCredsData(OwnerRegisterRequest ownerRegisterRequest);

    @Mapping(source = "username", target = "email")
    @Mapping(source = "mobile", target = "mobile")
    @Mapping(source = "userRole", target = "userRole", qualifiedByName = "mapUserRoleEnum")
    Owner mapOwnerData(OwnerRegisterRequest ownerRegisterRequest);

    @Named("mapUserRoleEnum")
    default UserRole mapUserRoleEnum(UserRoleEnum userRoleEnum) {
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
