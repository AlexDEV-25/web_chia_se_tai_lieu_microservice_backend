package com.example.interactionservice.repository;

import com.example.commondto.response.CommentAdminResponse;
import com.example.interactionservice.dto.response.CommentTotalAdminProjection;
import com.example.interactionservice.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByDocumentId(Long documentId);

    Page<Comment> findByDocumentIdAndParentIsNullAndHideFalse(Long documentId, Pageable pageable);

    Page<Comment> findByParentIdAndHideFalse(Long parentId, Pageable pageable);


    Optional<Comment> findByIdAndUserIdAndHideFalse(Long id, Long UserId);


    @Query("""
            SELECT new com.example.commondto.response.CommentAdminResponse(
            c.id,
            c.content
            )
            FROM Comment c
            WHERE c.createdAt >= :fromDate
            AND c.hide = false
            ORDER BY c.createdAt DESC
            """)
    List<CommentAdminResponse> findDocumentCommentsLast7Days(@Param("fromDate") LocalDateTime fromDate);

    @Query("""
                SELECT
                    c.documentId AS documentId,
                    c.documentTitle AS documentTitle,
                    COUNT(c) AS commentTotal
                FROM Comment c
                GROUP BY c.documentId, c.documentTitle
            """)
    List<CommentTotalAdminProjection> getTotalCommentOfDocument();
}
