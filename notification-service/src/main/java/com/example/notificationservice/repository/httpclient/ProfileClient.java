package com.example.notificationservice.repository.httpclient;


import com.example.notificationservice.dto.response.APIResponse;
import com.example.notificationservice.dto.response.UserFollowNotificationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "profile-service", url = "${app.services.profile.url}")
public interface ProfileClient {
    @PostMapping(value = "/api/internal/follows/follower/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<UserFollowNotificationResponse> getFollowerByUserId(@PathVariable Long userId);


}
