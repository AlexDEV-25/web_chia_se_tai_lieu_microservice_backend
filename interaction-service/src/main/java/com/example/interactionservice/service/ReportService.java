package com.example.interactionservice.service;


import com.example.interactionservice.constant.AppError;
import com.example.interactionservice.dto.request.ReportRequest;
import com.example.interactionservice.dto.response.ReportAdminResponse;
import com.example.interactionservice.dto.response.ReportDetailAdminResponse;
import com.example.interactionservice.dto.response.ReportUserResponse;
import com.example.interactionservice.exception.AppException;
import com.example.interactionservice.helper.GetUserIdByToken;
import com.example.interactionservice.mapper.ReportMapper;
import com.example.interactionservice.model.Report;
import com.example.interactionservice.repository.ReportRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportService {
    private final ReportRepository documentReportRepository;
    private final ReportMapper reportMapper;
    private final GetUserIdByToken getUserIdByToken;

    @PreAuthorize("hasRole('ADMIN')")
    public List<ReportDetailAdminResponse> findByDocumentId(Long documentId) {
        List<Report> reports = documentReportRepository.findByDocumentId(documentId);
        return reports.stream().map(reportMapper::documentReportToResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ReportAdminResponse> getAllDocumentReportSummary() {
        return documentReportRepository.getAllDocumentReportSummary();
    }

    @PreAuthorize("hasAuthority('REPORT')")
    public ReportUserResponse report(ReportRequest request) {
        Long userId = getUserIdByToken.get();

        Report report = Report.builder().reason(request.getReason())
                .createdAt(LocalDateTime.now()).build();

        if (documentReportRepository.existsByUserIdAndDocumentId(userId, request.getDocumentId())) {
            throw AppException.builder().appError(AppError.ALREADY_REPORTED).build();
        }
        Report saved = documentReportRepository.save(report);
        return reportMapper.documentReportToReportUserResponse(saved);
    }


}
