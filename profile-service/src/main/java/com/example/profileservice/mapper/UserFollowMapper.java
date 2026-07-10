package com.example.profileservice.mapper;

import com.example.profileservice.dto.response.UserFollowResponse;
import com.example.profileservice.model.UserFollow;
import com.example.response.UserFollowNotificationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserFollowMapper {

    @Mapping(source = "follower.id", target = "followerId")
    @Mapping(source = "follower.fullName", target = "followerName")
    @Mapping(source = "following.id", target = "followingId")
    @Mapping(source = "following.fullName", target = "followingName")
    @Mapping(source = "following.avatarUrl", target = "followingAvatar")
    UserFollowResponse userFollowToResponse(UserFollow entity);

    @Mapping(source = "follower.id", target = "followerId")
    UserFollowNotificationResponse userFollowToNotificationResponse(UserFollow entity);
}
