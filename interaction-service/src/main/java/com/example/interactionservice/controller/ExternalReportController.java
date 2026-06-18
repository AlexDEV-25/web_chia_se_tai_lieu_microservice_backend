package com.example.interactionservice.controller;

import com.example.interactionservice.dto.request.ReportRequest;
import com.example.interactionservice.dto.response.APIResponse;
import com.example.interactionservice.dto.response.ReportAdminResponse;
import com.example.interactionservice.dto.response.ReportDetailAdminResponse;
import com.example.interactionservice.dto.response.ReportUserResponse;
import com.example.interactionservice.service.ReportService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external/reports")
@AllArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    public APIResponse<ReportUserResponse> report(@RequestBody @Valid ReportRequest dto) {
        APIResponse<ReportUserResponse> apiResponse = new APIResponse<ReportUserResponse>();
        apiResponse.setResult(reportService.report(dto));
        return apiResponse;
    }

    @GetMapping("/admin/document/{documentId}")
    public APIResponse<ReportDetailAdminResponse> findByDocumentId(@PathVariable Long documentId) {
        APIResponse<ReportDetailAdminResponse> apiResponse = new APIResponse<ReportDetailAdminResponse>();
        apiResponse.setResultList(reportService.findByDocumentId(documentId));
        return apiResponse;
    }


    @GetMapping("/admin/document")
    public APIResponse<ReportAdminResponse> getAllDocumentReportSummary() {
        APIResponse<ReportAdminResponse> apiResponse = new APIResponse<ReportAdminResponse>();
        apiResponse.setResultList(reportService.getAllDocumentReportSummary());
        return apiResponse;
    }
}
