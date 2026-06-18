package com.example.chatservice.mapper;


import com.example.chatservice.dto.response.ChatMessageResponse;
import com.example.chatservice.model.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

    @Mapping(target = "me", ignore = true)
    @Mapping(target = "conversationId", source = "conversation.id")
    @Mapping(target = "userId", source = "sender.userId")
    @Mapping(target = "userName", source = "sender.fullName")
    @Mapping(target = "userAvatar", source = "sender.avatarUrl")
    ChatMessageResponse chatMessagetoChatMessageResponse(ChatMessage entity);
}
