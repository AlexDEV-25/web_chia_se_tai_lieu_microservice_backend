package com.example.interactionservice.controller;


import com.example.interactionservice.dto.request.CommentRequest;
import com.example.interactionservice.dto.response.APIResponse;
import com.example.interactionservice.dto.response.CommentResponse;
import com.example.interactionservice.dto.response.CommentTreeResponse;
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
    public APIResponse<CommentTreeResponse> getDocumentCommentTree(@PathVariable Long docId) {
        APIResponse<CommentTreeResponse> apiResponse = new APIResponse<CommentTreeResponse>();
        apiResponse.setResultList(commentService.getDocumentTree(docId));
        return apiResponse;
    }
	

    @PostMapping
    public APIResponse<CommentResponse> createMyComment(@RequestBody @Valid CommentRequest dto) {
        APIResponse<CommentResponse> apiResponse = new APIResponse<CommentResponse>();
        apiResponse.setResult(commentService.saveMyComment(dto));
        return apiResponse;
    }

    @PutMapping("/{id}")
    public APIResponse<CommentResponse> updateMyComment(@PathVariable Long id, @RequestBody @Valid CommentRequest dto) {
        APIResponse<CommentResponse> apiResponse = new APIResponse<CommentResponse>();
        apiResponse.setResult(commentService.updateMyComment(id, dto));
        return apiResponse;
    }

}
