package com.example.chatservice.model;

import com.example.chatservice.constant.ChatRole;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "participant_infos", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id",
        "conversation_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "avatar_url", nullable = false)
    private String avatarUrl;

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    @JsonBackReference
    private Conversation conversation;

    @Column(name = "last_seen")
    private LocalDateTime lastSeen;

    @Enumerated(EnumType.STRING)
    @Column(name = "chat_role")
    private ChatRole chatRole;
}
