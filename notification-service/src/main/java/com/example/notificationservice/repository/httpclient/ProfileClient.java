package com.example.authservice.repository.httpclient;

import com.example.authservice.dto.request.DisplayRequest;
import com.example.authservice.dto.request.UserDetailRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service", url = "${app.services.profile.url}")
public interface ProfileClient {
    @PostMapping(value = "/api/internal/users-detail/my-detail-info", produces = MediaType.APPLICATION_JSON_VALUE)
    void createUserDetail(@RequestBody @Valid UserDetailRequest dto);

    @PutMapping(value = "/api/internal/users-detail/my-detail-info/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    void hideUserDetail(@PathVariable Long userId, @RequestBody @Valid DisplayRequest dto);
}
