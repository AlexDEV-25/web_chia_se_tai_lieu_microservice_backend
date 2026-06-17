package com.example.profileservice.repository;


import com.example.profileservice.model.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {
    boolean existsByFollower_IdAndFollowing_Id(Long followId, Long followingId);

    List<UserFollow> findByFollower_Id(Long followerId);

    List<UserFollow> findByFollowing_Id(Long followingId);

    // Đếm số người FOLLOW mình (follower)
    long countByFollowing_Id(Long userId);

    // Đếm số người mình FOLLOW (following)
    long countByFollower_Id(Long userId);

    void deleteByFollower_IdAndFollowing_Id(Long followerId, Long followingId);
}
