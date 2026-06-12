package com.example.authservice.repository;

import com.example.authservice.model.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    Optional<User> findByIdAndHideFalse(Long Id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsById(@NonNull Long id);

    @Query(value = """
            	SELECT DATE(u.created_at) as stat_date, COUNT(u.id)
            	FROM users u
            	WHERE (u.hide = 0 OR u.hide IS NULL)
            	AND u.created_at >= :fromDate
            	GROUP BY DATE(u.created_at)
            	ORDER BY DATE(u.created_at)
            """, nativeQuery = true)
    List<Object[]> countUserByDay(@Param("fromDate") LocalDateTime fromDate);
}