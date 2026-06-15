package com.example.interactionservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingAdminResponse {
    private Long id;
    private String documentTitle;
    private Double average;
    private Long total;
}
