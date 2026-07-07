package com.example.statisticsservice.repository;

import com.example.commondto.response.APIResponse;
import com.example.commondto.response.DailyCountProjection;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "auth-service", url = "${app.services.auth.url}")
public interface AuthClient {
    @GetMapping(value = "/api/internal/users/users/last-7-days", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<DailyCountProjection> userLast7Days();
}
