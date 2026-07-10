package com.example.profileservice.mapper;


import com.example.profileservice.dto.response.UserBioResponse;
import com.example.profileservice.model.UserDetail;
import com.example.request.UserDetailRequest;
import com.example.response.UserDetailInfoResponse;
import com.example.response.UserDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDetailMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    UserDetail requestToUser(UserDetailRequest request);

    UserDetailResponse userToResponse(UserDetail entity);

    UserBioResponse userToUserBioResponse(UserDetail entity);

    UserDetailInfoResponse userToInfoResponse(UserDetail entity);


}
