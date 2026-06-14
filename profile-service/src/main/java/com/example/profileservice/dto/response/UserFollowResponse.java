package com.example.profileservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFollowResponse {
    private Long id;
    private Long followerId;
    private String followerName;
    private Long followingId;
    private String followingName;
    private String followingAvatar;
    private LocalDateTime createdAt;
}
