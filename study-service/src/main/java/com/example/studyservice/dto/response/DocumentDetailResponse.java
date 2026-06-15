package com.example.studyservice.dto.response;

import com.example.studyservice.constant.ContentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDetailResponse {
    private Long id;
    private String title;
    private String description;
    private String fileUrl;
    private String thumbnailUrl;
    private Long viewsCount;
    private Long downloadsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ContentStatus status;
    private boolean hide;
    private Long categoryId;
    private String categoryName;
    private Long userId;
    private String userName;
}
