package com.example.interactionservice.repository;


import com.example.interactionservice.dto.response.ReportAdminResponse;
import com.example.interactionservice.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByDocumentId(Long documentId);

    boolean existsByUserIdAndDocumentId(Long userId, Long documentId);

    @Query("""
                SELECT new com.example.interactionservice.dto.response.ReportAdminResponse(
                    r.documentId,
                    r.documentTitle,
                    COUNT(r)
                )
                FROM Report r
                GROUP BY  r.documentId,r.documentTitle
                ORDER BY COUNT(r) DESC
            """)
    List<ReportAdminResponse> getAllDocumentReportSummary();
}
