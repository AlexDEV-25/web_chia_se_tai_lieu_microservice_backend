package com.example.statisticsservice.controller;

import com.example.response.APIResponse;
import com.example.response.CategoryCountProjection;
import com.example.response.DailyCountProjection;
import com.example.statisticsservice.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/external/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/users/last-7-days")
    public APIResponse<DailyCountProjection> userLast7Days() {
        return APIResponse.<DailyCountProjection>builder()
                .resultList(statisticsService.userLast7Days()).build();
    }

    @GetMapping("/documents/last-7-days")
    public APIResponse<DailyCountProjection> documentLast7Days() {
        return APIResponse.<DailyCountProjection>builder()
                .resultList(statisticsService.documentLast7Days()).build();
    }

    @GetMapping("/documents/by-category")
    public APIResponse<CategoryCountProjection> documentByCategory() {
        return APIResponse.<CategoryCountProjection>builder()
                .resultList(statisticsService.documentByCategory()).build();
    }
}
