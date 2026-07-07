package com.example.interactionservice.controller;


import com.example.commondto.request.DisplayRequest;
import com.example.commondto.response.APIResponse;
import com.example.commondto.response.CommentDetailAdminResponse;
import com.example.interactionservice.dto.request.CommentRequest;
import com.example.interactionservice.dto.response.CommentTotalAdminProjection;
import com.example.interactionservice.dto.response.CommentTreeUserResponse;
import com.example.interactionservice.dto.response.CommentUserResponse;
import com.example.interactionservice.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external/comments")
@AllArgsConstructor
public class ExternalCommentController {
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
    public APIResponse<CommentTotalAdminProjection> getTotalCommentOfDocument() {
        APIResponse<CommentTotalAdminProjection> apiResponse = new APIResponse<>();
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
