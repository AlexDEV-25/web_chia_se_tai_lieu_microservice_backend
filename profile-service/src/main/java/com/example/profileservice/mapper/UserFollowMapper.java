package com.example.profileservice.mapper;

import com.example.profileservice.dto.response.UserFollowNotificationResponse;
import com.example.profileservice.dto.response.UserFollowResponse;
import com.example.profileservice.model.UserFollow;
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
    @Mapping(source = "follower.fullName", target = "followerName")
    UserFollowNotificationResponse userFollowToNotificationResponse(UserFollow entity);
}
