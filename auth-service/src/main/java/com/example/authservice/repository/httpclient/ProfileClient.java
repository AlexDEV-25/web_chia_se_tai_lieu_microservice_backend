package com.example.authservice.repository.httpclient;

import com.example.commondto.request.DisplayRequest;
import com.example.commondto.request.UserDetailRequest;
import com.example.commondto.response.APIResponse;
import com.example.commondto.response.UserDetailResponse;
import com.example.commonsecurity.configuration.CommonFeignConfiguration;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service", url = "${app.services.profile.url}", configuration = CommonFeignConfiguration.class)
public interface ProfileClient {
    @PostMapping(value = "/api/internal/users-detail/my-detail-info", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<UserDetailResponse> createUserDetail(@RequestBody @Valid UserDetailRequest dto);

    @PutMapping(value = "/api/internal/admin/users-detail/my-detail-info/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<UserDetailResponse> hideUserDetail(@PathVariable Long userId, @RequestBody @Valid DisplayRequest dto);
}
