package com.example.interactionservice.controller;

import com.example.commondto.response.APIResponse;
import com.example.interactionservice.dto.request.ReportRequest;
import com.example.interactionservice.dto.response.ReportAdminProjection;
import com.example.interactionservice.dto.response.ReportDetailAdminResponse;
import com.example.interactionservice.dto.response.ReportUserResponse;
import com.example.interactionservice.service.ReportService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external/reports")
@AllArgsConstructor
public class ExternalReportController {
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
    public APIResponse<ReportAdminProjection> getAllDocumentReportSummary() {
        APIResponse<ReportAdminProjection> apiResponse = new APIResponse<>();
        apiResponse.setResultList(reportService.getAllDocumentReportSummary());
        return apiResponse;
    }
}
