package com.example.chatservice.controller;


import com.example.chatservice.dto.response.APIResponse;
import com.example.chatservice.dto.response.ChatMessageResponse;
import com.example.chatservice.service.ChatMessageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat-messages")
@AllArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @GetMapping("/my-conversation-messages/{conversationId}")
    public APIResponse<ChatMessageResponse> getMyMessages(@PathVariable Long conversationId) {
        APIResponse<ChatMessageResponse> apiResponse = new APIResponse<ChatMessageResponse>();
        apiResponse.setResultList(chatMessageService.getMessages(conversationId));
        return apiResponse;
    }
}
