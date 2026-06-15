package com.example.interactionservice.controller;

import com.example.interactionservice.dto.request.DisplayRequest;
import com.example.interactionservice.dto.response.APIResponse;
import com.example.interactionservice.dto.response.CommentResponse;
import com.example.interactionservice.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments/admin")
@AllArgsConstructor
public class AdminCommentController {
    //    private final ChatService chatService;
    private final CommentService commentService;

//    @GetMapping("/filter-comment")
//    APIResponse<CommentResponse> filterCommnent(@RequestParam String type) {
//        APIResponse<CommentResponse> apiResponse = new APIResponse<CommentResponse>();
//        apiResponse.setResultList(chatService.filterCommnent(type));
//        return apiResponse;
//    }

    @GetMapping("/document")
    public APIResponse<CommentResponse> getAllDocumentComments() {
        APIResponse<CommentResponse> apiResponse = new APIResponse<CommentResponse>();
        apiResponse.setResultList(commentService.getAllDocumentComments());
        return apiResponse;
    }

    @PutMapping("/hide/{id}")
    public APIResponse<CommentResponse> hide(@PathVariable Long id, @RequestBody @Valid DisplayRequest dto) {
        APIResponse<CommentResponse> apiResponse = new APIResponse<CommentResponse>();
        apiResponse.setResult(commentService.hide(id, dto));
        return apiResponse;
    }
}
