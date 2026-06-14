package com.example.studyservice.repository;


import com.example.studyservice.constant.ContentStatus;
import com.example.studyservice.model.Favorite;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("""
            SELECT f
            	FROM DocumentFavorite f
            	JOIN f.document d
            	WHERE
            		f.user_id = :userId
            		AND d.status = :status
            		AND d.hide = false
            """)
    List<Favorite> findByUserIdAndDocumentFit(@Param("userId") Long userId,
                                              @Param("status") ContentStatus status);

    Optional<Favorite> findByUserIdAndDocument_Id(Long userId, Long documentId);

    boolean existsByUserIdAndDocument_Id(Long userId, Long documentId);

    void deleteByDocument_Id(Long documentId);
}
