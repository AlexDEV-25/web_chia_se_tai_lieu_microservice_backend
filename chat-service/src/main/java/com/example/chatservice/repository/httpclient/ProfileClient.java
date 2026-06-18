package com.example.chatservice.repository.httpclient;

import com.example.chatservice.dto.request.ConnectRequest;
import com.example.chatservice.dto.response.APIResponse;
import com.example.chatservice.dto.response.UserDetailInfoResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service", url = "${app.services.profile.url}")
public interface ProfileClient {
    @GetMapping(value = "/api/internal/users-detail/detail-info/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<UserDetailInfoResponse> getUserDetail(@PathVariable Long userId);

    @PutMapping(value = "/api/internal/users-detail/detail-info/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<Void> changeConnectStatus(@PathVariable Long userId, @RequestBody @Valid ConnectRequest dto);
}
