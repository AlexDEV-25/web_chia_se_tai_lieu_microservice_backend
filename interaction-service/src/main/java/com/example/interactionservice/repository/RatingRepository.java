package com.example.interactionservice.repository;


import com.example.commondto.response.RatingSummaryResponse;
import com.example.interactionservice.dto.response.RatingAdminProjection;
import com.example.interactionservice.dto.response.RatingDetailAdminProjection;
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
                SELECT new com.example.commondto.response.RatingSummaryResponse(
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
                SELECT
                    r.documentId AS id,
                    r.documentTitle AS documentTitle,
                    COALESCE(AVG(r.rating), 0) AS average,
                    COUNT(r) AS total
                FROM Rating r
                GROUP BY r.documentId, r.documentTitle
                ORDER BY
                    COALESCE(AVG(r.rating), 0) DESC,
                    COUNT(r) DESC
            """)
    List<RatingAdminProjection> getAllDocumentRatingSummary();

    @Query("""
                SELECT
                    r.documentId AS documentId,
                    r.documentTitle AS documentTitle,
                    SUM(CASE WHEN r.rating = 1 THEN 1 ELSE 0 END) AS star1,
                    SUM(CASE WHEN r.rating = 2 THEN 1 ELSE 0 END) AS star2,
                    SUM(CASE WHEN r.rating = 3 THEN 1 ELSE 0 END) AS star3,
                    SUM(CASE WHEN r.rating = 4 THEN 1 ELSE 0 END) AS star4,
                    SUM(CASE WHEN r.rating = 5 THEN 1 ELSE 0 END) AS star5
                FROM Rating r
                WHERE r.documentId = :documentId
                GROUP BY r.documentId, r.documentTitle
            """)
    RatingDetailAdminProjection getDocumentRatingDetail(
            @Param("documentId") Long documentId
    );
}
