package com.example.chatservice.mapper;

import com.example.chatservice.dto.response.ConversationResponse;
import com.example.chatservice.model.Conversation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConversationMapper {
    @Mapping(target = "conversationAvatar", ignore = true)
    @Mapping(target = "conversationName", ignore = true)
    @Mapping(target = "participantInfos", ignore = true)
    ConversationResponse toConversationResponse(Conversation conversation);
}
