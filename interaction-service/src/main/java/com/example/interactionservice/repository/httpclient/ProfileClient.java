package com.example.studyservice.repository.httpclient;

import com.example.studyservice.dto.response.APIResponse;
import com.example.studyservice.dto.response.UserDetailInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service", url = "${app.services.profile.url}")
public interface ProfileClient {
    @GetMapping(value = "/api/internal/users-detail/detail-info/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<UserDetailInfoResponse> getUserDetail(@PathVariable Long userId);

}
