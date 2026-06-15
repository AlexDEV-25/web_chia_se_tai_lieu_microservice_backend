package com.example.interactionservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingDetailAdminResponse {
    private Long documentId;
    private String documentTitle;
    private Long star1;
    private Long star2;
    private Long star3;
    private Long star4;
    private Long star5;
}
