package com.example.profileservice.repository;

import com.example.profileservice.dto.response.UserBioProjection;
import com.example.profileservice.model.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {

    Optional<UserDetail> findByUserIdAndHideFalse(Long userId);

    Optional<UserDetail> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    @Query("""
                SELECT
                    u.id AS id,
                    u.fullName AS fullName,
                    u.avatarUrl AS avatarUrl,
                    u.bio AS bio,
                    u.status AS status
                FROM UserDetail u
                WHERE u.hide = false
                  AND u.fullName LIKE CONCAT('%', :keyword, '%')
            """)
    List<UserBioProjection> search(@Param("keyword") String keyword);
}
