package com.example.interactionservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CommentTotalAdminResponse {
    private Long documentId;
    private String documentTitle;
    private Long commentTotal;
}
