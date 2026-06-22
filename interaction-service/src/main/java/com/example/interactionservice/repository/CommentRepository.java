package com.example.interactionservice.repository;

import com.example.commondto.response.CommentAdminResponse;
import com.example.interactionservice.dto.response.CommentTotalAdminResponse;
import com.example.interactionservice.model.Comment;
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

    List<Comment> findByDocumentIdAndHideFalseOrderByLevelAscCreatedAtAsc(Long documentId);

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
            SELECT new com.example.commondto.response.CommentTotalAdminResponse(
            c.documentId,
            c.documentTitle,
            COUNT(c)
            )
            FROM Comment c
            GROUP BY c.documentId
            """)
    List<CommentTotalAdminResponse> getTotalCommentOfDocument();
}
