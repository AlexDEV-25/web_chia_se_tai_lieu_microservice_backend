package com.example.chatservice.controller;

import com.example.chatservice.dto.response.APIResponse;
import com.example.chatservice.dto.response.ChatHistoryResponse;
import com.example.chatservice.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/external/chats")
@AllArgsConstructor
public class ExternalChatController {
    private final ChatService chatService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    APIResponse<String> chat(@RequestParam(value = "image", required = false) MultipartFile file,
                             @RequestParam("message") String request) {
        APIResponse<String> apiResponse = new APIResponse<>();
        apiResponse.setResult(chatService.chat(file, request));
        return apiResponse;
    }

    @GetMapping()
    public APIResponse<ChatHistoryResponse> getChatHistory() {
        APIResponse<ChatHistoryResponse> apiResponse = new APIResponse<>();
        apiResponse.setResultList(chatService.getChatHistory());
        return apiResponse;
    }
}
