package com.example.interactionservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "level")
    private Long level;

    @Column(name = "hide")
    private boolean hide;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "parent")
    @JsonManagedReference
    private List<Comment> replies;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private Comment parent;

    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(name = "document_title", nullable = false)
    private String documentTitle;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_avatar", nullable = false)
    private String userAvatar;

}
