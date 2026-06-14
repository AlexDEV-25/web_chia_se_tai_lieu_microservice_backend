package com.example.studyservice.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentResponse {
    private Long id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String userId;
    private String username;
    private Long viewsCount;
    private Long downloadsCount;
    private boolean favorite;
}
