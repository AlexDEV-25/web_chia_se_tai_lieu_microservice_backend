package com.example.interactionservice.repository;


import com.example.interactionservice.dto.response.ReportAdminProjection;
import com.example.interactionservice.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByDocumentId(Long documentId);

    List<Report> findByUserId(Long UserId);

    boolean existsByUserIdAndDocumentId(Long userId, Long documentId);

    @Query("""
                SELECT
                    r.documentId AS id,
                    r.documentTitle AS documentTitle,
                    COUNT(r) AS total
                FROM Report r
                GROUP BY r.documentId, r.documentTitle
                ORDER BY COUNT(r) DESC
            """)
    List<ReportAdminProjection> getAllDocumentReportSummary();
}