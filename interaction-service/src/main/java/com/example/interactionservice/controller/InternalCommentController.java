package com.example.interactionservice.controller;

import com.example.interactionservice.dto.response.APIResponse;
import com.example.interactionservice.dto.response.CommentAdminResponse;
import com.example.interactionservice.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/comments/")
@AllArgsConstructor
public class InternalCommentController {
    private final CommentService commentService;

    @GetMapping("/admin/7-days")
    public APIResponse<CommentAdminResponse> findDocumentCommentsLast7Days() {
        APIResponse<CommentAdminResponse> apiResponse = new APIResponse<>();
        apiResponse.setResultList(commentService.findDocumentCommentsLast7Days());
        return apiResponse;
    }
}
