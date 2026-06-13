package com.example.authservice.repository.httpclient;

import com.example.authservice.dto.request.UserDetailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service", url = "${app.services.profile.url}")
public interface ProfileClient {
    @PostMapping(value = "/api/internal/users-detail/my-info", produces = MediaType.APPLICATION_JSON_VALUE)
    void createUserDetail(@RequestBody UserDetailRequest dto);
}
