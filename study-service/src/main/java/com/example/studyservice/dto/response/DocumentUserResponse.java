package com.example.studyservice.dto.response;

import com.example.ContentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentUserResponse {
    private Long id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private Long viewsCount;
    private Long downloadsCount;
    private ContentStatus status;
}
