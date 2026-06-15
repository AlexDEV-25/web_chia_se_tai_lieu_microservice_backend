package com.example.interactionservice.controller;

import com.example.interactionservice.dto.request.ReportRequest;
import com.example.interactionservice.dto.response.APIResponse;
import com.example.interactionservice.dto.response.ReportUserResponse;
import com.example.interactionservice.service.ReportService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@AllArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    public APIResponse<ReportUserResponse> report(@RequestBody @Valid ReportRequest dto) {
        APIResponse<ReportUserResponse> apiResponse = new APIResponse<ReportUserResponse>();
        apiResponse.setResult(reportService.report(dto));
        return apiResponse;
    }
}
