package com.example.profileservice.mapper;

import com.example.profileservice.dto.response.UserFollowResponse;
import com.example.profileservice.model.UserFollow;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserFollowMapper {
	
    UserFollowResponse userFollowToResponse(UserFollow entity);
}
