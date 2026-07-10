package com.example.chatservice.controller;

import com.example.AppError;
import com.example.chatservice.dto.request.ChatMessageRequest;
import com.example.chatservice.service.ChatMessageService;
import com.example.AppException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ExternalSendMessageController {
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void sendMessage(@Payload @Valid ChatMessageRequest request) {
        try {
            chatMessageService.createMessage(request);
        } catch (Exception e) {
            throw AppException.builder().appError(AppError.MESSAGE_SEND_FAILED).build();
        }
    }
}
