package com.example.interactionservice.repository.httpclient;


import com.example.configuration.CommonFeignConfiguration;
import com.example.response.APIResponse;
import com.example.response.UserDetailInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service", url = "${app.services.profile.url}", configuration = CommonFeignConfiguration.class)
public interface ProfileClient {
    @GetMapping(value = "/api/internal/users-detail/detail-info/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<UserDetailInfoResponse> getUserDetail(@PathVariable Long userId);

}
