package com.example.chatservice.event;


import com.example.chatservice.dto.response.ChatMessageResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageCreatedEvent {
    private ChatMessageResponse message;
}
