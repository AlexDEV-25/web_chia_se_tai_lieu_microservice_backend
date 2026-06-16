package com.example.profileservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_follow"
        , uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_follower_following",
                columnNames = {"follower_id", "following_id"}
        )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFollow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "follower_id", nullable = false)
    private Long followerId;

    @Column(name = "follower_name", nullable = false)
    private String followerName;

    @Column(name = "following_id", nullable = false)
    private Long followingId;

    @Column(name = "following_name", nullable = false)
    private String followingName;

    private LocalDateTime createdAt;
}
