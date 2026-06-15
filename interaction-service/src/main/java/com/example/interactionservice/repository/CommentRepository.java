package com.example.interactionservice.repository;

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
    List<Comment> findByDocumentIdAndHideFalse(Long documentId);

    List<Comment> findByDocumentIdAndHideFalseOrderByLevelAscCreatedAtAsc(Long documentId);

    Optional<Comment> findByIdAndUserIdAndHideFalse(Long id, Long UserId);

    void deleteByDocumentId(Long documentId);

    @Query("""
            SELECT c
            FROM Comment c
            WHERE c.createdAt >= :fromDate
            AND c.hide = false
            ORDER BY c.createdAt DESC
            """)
    List<Comment> findDocumentCommentsLast7Days(@Param("fromDate") LocalDateTime fromDate);
}
