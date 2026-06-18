package com.example.interactionservice.repository;


import com.example.interactionservice.dto.response.RatingAdminResponse;
import com.example.interactionservice.dto.response.RatingDetailAdminResponse;
import com.example.interactionservice.dto.response.RatingSummaryResponse;
import com.example.interactionservice.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    boolean existsByUserIdAndDocumentId(Long userId, Long documentId);

    @Query("""
                SELECT new com.example.interactionservice.dto.response.RatingSummaryResponse(
                    AVG(r.rating),
                    COUNT(r.id)
                )
                FROM Rating r
                WHERE r.documentId = :documentId
            """)
    RatingSummaryResponse getDocumentRatingSummary(@Param("documentId") Long documentId);

    @Query("""
                SELECT COALESCE(r.rating, 0)
                FROM Rating r
                WHERE r.documentId = :documentId
                AND r.userId = :userId
            """)
    Integer getMyDocumentRating(@Param("documentId") Long documentId, @Param("userId") Long userId);

    @Query("""
                SELECT new com.example.interactionservice.dto.response.RatingAdminResponse(
                    r.documentId,
                    r.documentTitle,
                    COALESCE(AVG(r.rating), 0),
                    COUNT(r)
                )
                FROM Rating r
                GROUP BY  r.documentId, r.documentTitle
                ORDER BY
                    COALESCE(AVG(r.rating), 0) DESC,
                    COUNT(r) DESC
            """)
    List<RatingAdminResponse> getAllDocumentRatingSummary();

    @Query("""
                SELECT new com.example.interactionservice.dto.response.RatingDetailAdminResponse(
                    r.documentId,
                    r.documentTitle,
                    SUM(CASE WHEN r.rating = 1 THEN 1 ELSE 0 END),
                    SUM(CASE WHEN r.rating = 2 THEN 1 ELSE 0 END),
                    SUM(CASE WHEN r.rating = 3 THEN 1 ELSE 0 END),
                    SUM(CASE WHEN r.rating = 4 THEN 1 ELSE 0 END),
                    SUM(CASE WHEN r.rating = 5 THEN 1 ELSE 0 END)
                )
                FROM Rating r
                WHERE r.documentId = :documentId
                GROUP BY  r.documentId,r.documentTitle
            """)
    RatingDetailAdminResponse getDocumentRatingDetail(@Param("documentId") Long documentId);
}
