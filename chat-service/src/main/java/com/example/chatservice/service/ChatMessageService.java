package com.example.chatservice.service;

import com.example.AppError;
import com.example.chatservice.dto.request.ChatMessageRequest;
import com.example.chatservice.dto.response.ChatMessageResponse;
import com.example.chatservice.event.MessageCreatedEvent;
import com.example.chatservice.mapper.ChatMessageMapper;
import com.example.chatservice.model.ChatMessage;
import com.example.chatservice.model.Conversation;
import com.example.chatservice.model.ParticipantInfo;
import com.example.chatservice.repository.ChatMessageRepository;
import com.example.chatservice.repository.ConversationRepository;
import com.example.chatservice.repository.ParticipantInfoRepository;
import com.example.commonexception.exception.AppException;
import com.example.commonsecurity.helper.GetUserIdByToken;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ConversationRepository conversationRepository;
    private final ParticipantInfoRepository participantInfoRepository;
    private final ApplicationEventPublisher publisher;
    private final ChatMessageMapper chatMessageMapper;

    @PreAuthorize("hasAuthority('GET_MESSAGE')")
    public List<ChatMessageResponse> getMessages(Long conversationId) {
        Long userId = GetUserIdByToken.get();

        if (userId == 0L) {
            throw AppException.builder().appError(AppError.USER_NOT_FOUND).build();
        }

        List<ChatMessage> messages = chatMessageRepository.findAllByConversation_IdOrderByCreatedAtAsc(conversationId);
        return messages.stream().map((message) -> toChatMessageResponse(message, userId)).toList();
    }

    @PreAuthorize("hasAuthority('CREATE_MESSAGE')")
    public void createMessage(ChatMessageRequest request) {
        Long userId = GetUserIdByToken.get();

        if (userId == 0L) {
            throw AppException.builder().appError(AppError.USER_NOT_FOUND).build();
        }

        Conversation conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> AppException.builder().appError(AppError.CONVERSATION_NOT_FOUND).build());


        ParticipantInfo participantInfo = participantInfoRepository.findByConversation_IdAndUserId(conversation.getId(),
                userId).orElseThrow(() -> AppException.builder().appError(AppError.NOT_CONVERSATION_MEMBER).build());

        ChatMessage message = ChatMessage.builder()
                .message(request.getMessage())
                .conversation(conversation)
                .sender(participantInfo)
                .createdAt(LocalDateTime.now()).build();

        ChatMessage saved = chatMessageRepository.save(message);

        ChatMessageResponse response = toChatMessageResponse(saved, userId);

        publisher.publishEvent(new MessageCreatedEvent(response));

    }

    private ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage, Long userId) {
        ChatMessageResponse chatMessageResponse = chatMessageMapper.chatMessagetoChatMessageResponse(chatMessage);
        chatMessageResponse.setMe(userId.equals(chatMessage.getSender().getId()));
        return chatMessageResponse;
    }

}
