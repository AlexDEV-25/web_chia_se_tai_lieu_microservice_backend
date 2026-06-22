package com.example.chatservice.dto.response;

import com.example.ConversationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationResponse {
    private Long id;
    private ConversationType type;
    private String conversationAvatar;// avatar của người mình đang nhắn tin
    private String conversationName;// tên của người mình đang nhắn tin
    private List<ParticipantInfoResponse> participantInfos;
}
