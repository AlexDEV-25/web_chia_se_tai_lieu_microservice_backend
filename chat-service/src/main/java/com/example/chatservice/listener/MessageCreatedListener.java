package com.example.chatservice.listener;

import com.example.chatservice.event.MessageCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageCreatedListener {
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleMessageCreated(MessageCreatedEvent event) {

        messagingTemplate.convertAndSend("/topic/conversation/" + event.getMessage().getConversationId(),
                event.getMessage());
    }
}
