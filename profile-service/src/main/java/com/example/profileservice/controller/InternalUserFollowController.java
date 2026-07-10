package com.example.profileservice.controller;

import com.example.profileservice.dto.response.UserFollowNotificationResponse;
import com.example.profileservice.service.UserFollowService;
import com.example.response.APIResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/follows")
@AllArgsConstructor
public class InternalUserFollowController {
    private final UserFollowService userFollowService;

    @GetMapping("/follower/{userId}")
    public APIResponse<UserFollowNotificationResponse> getFollowerByUserId(@PathVariable Long userId) {
        APIResponse<UserFollowNotificationResponse> apiResponse = new APIResponse<UserFollowNotificationResponse>();
        apiResponse.setResultList(userFollowService.getFollowerByUserId(userId));
        return apiResponse;
    }
}
