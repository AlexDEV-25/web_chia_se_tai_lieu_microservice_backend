package com.example.interactionservice.controller;


import com.example.interactionservice.dto.request.CommentRequest;
import com.example.interactionservice.dto.request.DisplayRequest;
import com.example.interactionservice.dto.response.*;
import com.example.interactionservice.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/document/{docId}")
    public APIResponse<CommentTreeUserResponse> getDocumentCommentTree(@PathVariable Long docId) {
        APIResponse<CommentTreeUserResponse> apiResponse = new APIResponse<CommentTreeUserResponse>();
        apiResponse.setResultList(commentService.getDocumentTree(docId));
        return apiResponse;
    }


    @PostMapping
    public APIResponse<CommentUserResponse> createMyComment(@RequestBody @Valid CommentRequest dto) {
        APIResponse<CommentUserResponse> apiResponse = new APIResponse<CommentUserResponse>();
        apiResponse.setResult(commentService.saveMyComment(dto));
        return apiResponse;
    }

    @PutMapping("/{id}")
    public APIResponse<CommentUserResponse> updateMyComment(@PathVariable Long id, @RequestBody @Valid CommentRequest dto) {
        APIResponse<CommentUserResponse> apiResponse = new APIResponse<CommentUserResponse>();
        apiResponse.setResult(commentService.updateMyComment(id, dto));
        return apiResponse;
    }

    @GetMapping("/admin")
    public APIResponse<CommentTotalAdminResponse> getTotalCommentOfDocument() {
        APIResponse<CommentTotalAdminResponse> apiResponse = new APIResponse<>();
        apiResponse.setResultList(commentService.getTotalCommentOfDocument());
        return apiResponse;
    }

    @GetMapping("/admin/detail/{documentId}")
    public APIResponse<CommentDetailAdminResponse> getDetailDocumentComments(@PathVariable Long documentId) {
        APIResponse<CommentDetailAdminResponse> apiResponse = new APIResponse<>();
        apiResponse.setResultList(commentService.getDetailDocumentComments(documentId));
        return apiResponse;
    }

    @PutMapping("/admin/hide/{id}")
    public APIResponse<CommentDetailAdminResponse> hide(@PathVariable Long id, @RequestBody @Valid DisplayRequest dto) {
        APIResponse<CommentDetailAdminResponse> apiResponse = new APIResponse<>();
        apiResponse.setResult(commentService.hide(id, dto));
        return apiResponse;
    }

}
