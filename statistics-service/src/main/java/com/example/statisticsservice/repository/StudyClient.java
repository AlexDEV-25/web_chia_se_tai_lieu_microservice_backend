package com.example.statisticsservice.repository;

import com.example.configuration.CommonFeignConfiguration;
import com.example.response.APIResponse;
import com.example.response.CategoryCountProjection;
import com.example.response.DailyCountProjection;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "study-service", url = "${app.services.study.url}", configuration = CommonFeignConfiguration.class)
public interface StudyClient {
    @GetMapping(value = "/api/internael/documents/last-7-days", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<DailyCountProjection> documentLast7Days();

    @GetMapping(value = "/api/internal/documents/by-category", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<CategoryCountProjection> documentByCategory();

}
