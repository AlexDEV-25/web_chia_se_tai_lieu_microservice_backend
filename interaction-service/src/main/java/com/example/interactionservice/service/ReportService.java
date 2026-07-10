package com.example.interactionservice.service;


import com.example.AppError;
import com.example.AppException;
import com.example.helper.GetUserIdByToken;
import com.example.interactionservice.dto.request.ReportRequest;
import com.example.interactionservice.dto.response.ReportAdminProjection;
import com.example.interactionservice.dto.response.ReportDetailAdminResponse;
import com.example.interactionservice.dto.response.ReportUserResponse;
import com.example.interactionservice.mapper.ReportMapper;
import com.example.interactionservice.model.Report;
import com.example.interactionservice.repository.ReportRepository;
import com.example.interactionservice.repository.httpclient.ProfileClient;
import com.example.interactionservice.repository.httpclient.StudyClient;
import com.example.response.DocumentInfoResponse;
import com.example.response.UserDetailInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository documentReportRepository;
    private final ReportMapper reportMapper;
    private final StudyClient studyClient;
    private final ProfileClient profileClient;

    @PreAuthorize("hasRole('ADMIN')")
    public List<ReportDetailAdminResponse> findByDocumentId(Long documentId) {
        List<Report> reports = documentReportRepository.findByDocumentId(documentId);
        return reports.stream().map(reportMapper::documentReportToResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ReportAdminProjection> getAllDocumentReportSummary() {
        return documentReportRepository.getAllDocumentReportSummary();
    }

    @PreAuthorize("hasAuthority('REPORT')")
    public ReportUserResponse report(ReportRequest request) {
        Long userId = GetUserIdByToken.get();
        DocumentInfoResponse doc = studyClient.getAllPublicDocumentsForInteraction(request.getDocumentId()).getResult();
        UserDetailInfoResponse user = profileClient.getUserDetail(userId).getResult();

        Report report = Report
                .builder()
                .reason(request.getReason())
                .userId(userId)
                .fullName(user.getFullName())
                .documentId(request.getDocumentId())
                .documentTitle(doc.getTitle())
                .createdAt(LocalDateTime.now()).build();

        if (documentReportRepository.existsByUserIdAndDocumentId(userId, request.getDocumentId())) {
            throw AppException.builder().appError(AppError.ALREADY_REPORTED).build();
        }
        Report saved = documentReportRepository.save(report);
        return reportMapper.documentReportToReportUserResponse(saved);
    }


}
