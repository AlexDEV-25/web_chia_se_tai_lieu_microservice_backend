package com.example.interactionservice.controller;


import com.example.interactionservice.dto.request.CommentRequest;
import com.example.interactionservice.dto.response.APIResponse;
import com.example.interactionservice.dto.response.CommentTreeUserResponse;
import com.example.interactionservice.dto.response.CommentUserResponse;
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

}
