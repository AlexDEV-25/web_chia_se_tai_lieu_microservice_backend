package com.example.chatservice.controller;

import com.example.AppError;
import com.example.chatservice.dto.request.ConversationGroupRequest;
import com.example.chatservice.dto.request.ConversationRequest;
import com.example.chatservice.dto.response.ConversationResponse;
import com.example.chatservice.service.ConversationService;
import com.example.AppException;
import com.example.response.APIResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/external/conversations")
@AllArgsConstructor
public class ExternalConversationController {
    private final ConversationService conversationService;

    @PostMapping("/direct")
    public APIResponse<ConversationResponse> createDirectConversation(@RequestBody @Valid ConversationRequest dto) {
        APIResponse<ConversationResponse> apiResponse = new APIResponse<>();
        apiResponse.setResult(conversationService.createDirectConversation(dto));
        return apiResponse;
    }

    @PostMapping(value = "/group", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public APIResponse<ConversationResponse> createGroupConversation(
            @RequestPart(value = "avt", required = false) MultipartFile avt, @RequestPart("data") String dataJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ConversationGroupRequest dto = mapper.readValue(dataJson, ConversationGroupRequest.class);

            APIResponse<ConversationResponse> apiResponse = new APIResponse<>();
            apiResponse.setResult(conversationService.createGroupConversation(avt, dto));
            return apiResponse;

        } catch (Exception e) {
            throw AppException.builder().appError(AppError.INVALID_JSON_FORMAT).build();
        }
    }

    @GetMapping("/my-conversations")
    public APIResponse<ConversationResponse> getMyConversation() {
        APIResponse<ConversationResponse> apiResponse = new APIResponse<>();
        apiResponse.setResultList(conversationService.getMyConversations());
        return apiResponse;
    }

    @GetMapping("/search")
    public APIResponse<ConversationResponse> search(@RequestParam String keyword) {
        APIResponse<ConversationResponse> apiResponse = new APIResponse<>();
        apiResponse.setResultList(conversationService.search(keyword));
        return apiResponse;
    }

    @GetMapping("/detail/{id}")
    public APIResponse<ConversationResponse> getDetailConversation(@PathVariable Long id) {
        APIResponse<ConversationResponse> apiResponse = new APIResponse<>();
        apiResponse.setResult(conversationService.getDetailConversation(id));
        return apiResponse;
    }
}
