package com.example.interactionservice.controller;


import com.example.interactionservice.dto.response.APIResponse;
import com.example.interactionservice.dto.response.ReportAdminResponse;
import com.example.interactionservice.dto.response.ReportDetailAdminResponse;
import com.example.interactionservice.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports/admin")
@AllArgsConstructor
public class AdminReportController {
    private final ReportService reportService;

    @GetMapping("/document/{documentId}")
    public APIResponse<ReportDetailAdminResponse> findByDocumentId(@PathVariable Long documentId) {
        APIResponse<ReportDetailAdminResponse> apiResponse = new APIResponse<ReportDetailAdminResponse>();
        apiResponse.setResultList(reportService.findByDocumentId(documentId));
        return apiResponse;
    }


    @GetMapping("/document")
    public APIResponse<ReportAdminResponse> getAllDocumentReportSummary() {
        APIResponse<ReportAdminResponse> apiResponse = new APIResponse<ReportAdminResponse>();
        apiResponse.setResultList(reportService.getAllDocumentReportSummary());
        return apiResponse;
    }
	
}
