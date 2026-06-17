package com.example.interactionservice.controller;

import com.example.interactionservice.dto.request.DisplayRequest;
import com.example.interactionservice.dto.response.APIResponse;
import com.example.interactionservice.dto.response.CommentAdminResponse;
import com.example.interactionservice.dto.response.CommentDetailAdminResponse;
import com.example.interactionservice.dto.response.CommentTotalAdminResponse;
import com.example.interactionservice.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments/admin")
@AllArgsConstructor
public class AdminCommentController {
    private final CommentService commentService;

    @GetMapping
    public APIResponse<CommentTotalAdminResponse> getTotalCommentOfDocument() {
        APIResponse<CommentTotalAdminResponse> apiResponse = new APIResponse<>();
        apiResponse.setResultList(commentService.getTotalCommentOfDocument());
        return apiResponse;
    }

    @GetMapping("/7-days")
    public APIResponse<CommentAdminResponse> findDocumentCommentsLast7Days() {
        APIResponse<CommentAdminResponse> apiResponse = new APIResponse<>();
        apiResponse.setResultList(commentService.findDocumentCommentsLast7Days());
        return apiResponse;
    }

    @GetMapping("/detail/{documentId}")
    public APIResponse<CommentDetailAdminResponse> getDetailDocumentComments(@PathVariable Long documentId) {
        APIResponse<CommentDetailAdminResponse> apiResponse = new APIResponse<>();
        apiResponse.setResultList(commentService.getDetailDocumentComments(documentId));
        return apiResponse;
    }

    @PutMapping("/hide/{id}")
    public APIResponse<CommentDetailAdminResponse> hide(@PathVariable Long id, @RequestBody @Valid DisplayRequest dto) {
        APIResponse<CommentDetailAdminResponse> apiResponse = new APIResponse<>();
        apiResponse.setResult(commentService.hide(id, dto));
        return apiResponse;
    }
}
