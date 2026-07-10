package com.example.chatservice.service;


import com.example.AppError;
import com.example.ChatRole;
import com.example.ConversationType;
import com.example.chatservice.dto.request.ParticipantInfoRequest;
import com.example.chatservice.dto.response.ParticipantInfoResponse;
import com.example.chatservice.mapper.ParticipantInfoMapper;
import com.example.chatservice.model.Conversation;
import com.example.chatservice.model.ParticipantInfo;
import com.example.chatservice.repository.ParticipantInfoRepository;
import com.example.chatservice.repository.httpclient.ProfileClient;
import com.example.AppException;
import com.example.helper.GetUserIdByToken;
import com.example.response.UserDetailInfoResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ParticipantInfoService {
    private final ParticipantInfoRepository participantInfoRepository;
    private final ParticipantInfoMapper participantInfoMapper;
    private final ProfileClient profileClient;

    @PreAuthorize("hasAuthority('UPDATE_LAST_SEEN')")
    public ParticipantInfoResponse updateLastSeen(Long id) {
        Long userId = GetUserIdByToken.get();
        ParticipantInfo participantInfo = participantInfoRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> AppException.builder().appError(AppError.PARTICIPANT_INFO_NOT_FOUND).build());
        participantInfo.setLastSeen(LocalDateTime.now());
        ParticipantInfo saved = participantInfoRepository.save(participantInfo);
        return participantInfoMapper.entityToResponse(saved);
    }

    @PreAuthorize("hasAuthority('ADD_MEMBER')")
    public ParticipantInfoResponse addMember(ParticipantInfoRequest request) {
        Long userId = GetUserIdByToken.get();

        ParticipantInfo participantInfo = findByUserIdAndConversationId(userId, request.getConversationId());
        if (participantInfo.getChatRole() == ChatRole.MEMBER) {
            throw AppException.builder().appError(AppError.ACCESS_DENIED).build();
        }
        Conversation conversation = participantInfo.getConversation();

        if (conversation.getType() == ConversationType.DIRECT) {
            throw AppException.builder().appError(AppError.CANNOT_ADD_MEMBER).build();
        }

        UserDetailInfoResponse newMember = profileClient.getUserDetail(request.getUserId()).getResult();

        ParticipantInfo newParticipantInfo = ParticipantInfo
                .builder()
                .userId(request.getUserId())
                .avatarUrl(newMember.getAvatarUrl())
                .fullName(newMember.getFullName())
                .conversation(conversation)
                .chatRole(ChatRole.MEMBER).build();

        ParticipantInfo saved = participantInfoRepository.save(newParticipantInfo);
        return participantInfoMapper.entityToResponse(saved);
    }

    @PreAuthorize("hasAuthority('DELETE_MEMBER')")
    public void deleteMember(Long deleteUserId, Long conversationId) {
        Long userId = GetUserIdByToken.get();

        ParticipantInfo myParticipantInfo = findByUserIdAndConversationId(userId, conversationId);

        ParticipantInfo userParticipantInfo = findByUserIdAndConversationId(deleteUserId, conversationId);

        if (!myParticipantInfo.getConversation().getId().equals(userParticipantInfo.getConversation().getId())) {
            throw AppException.builder().appError(AppError.NOT_CONVERSATION_MEMBER).build();
        }

        if (myParticipantInfo.getConversation().getType() == ConversationType.DIRECT) {
            throw AppException.builder().appError(AppError.CANNOT_REMOVE_MEMBER).build();
        }

        if (myParticipantInfo.getChatRole() == ChatRole.MEMBER
                || (userParticipantInfo.getChatRole() == myParticipantInfo.getChatRole())
                || (userParticipantInfo.getChatRole() == ChatRole.MANAGER)) {
            throw AppException.builder().appError(AppError.ACCESS_DENIED).build();
        }
        participantInfoRepository.delete(userParticipantInfo);
    }

    @PreAuthorize("hasAuthority('CHANGE_ROLE')")
    public ParticipantInfoResponse changeRole(@Valid ParticipantInfoRequest dto) {

        Long userId = GetUserIdByToken.get();

        ParticipantInfo myParticipantInfo = findByUserIdAndConversationId(userId, dto.getConversationId());

        ParticipantInfo userParticipantInfo = findByUserIdAndConversationId(dto.getUserId(), dto.getConversationId());

        if (myParticipantInfo.getChatRole() != ChatRole.MANAGER) {
            throw AppException.builder().appError(AppError.ACCESS_DENIED).build();
        }

        if (!myParticipantInfo.getConversation().getId().equals(userParticipantInfo.getConversation().getId())) {
            throw AppException.builder().appError(AppError.NOT_CONVERSATION_MEMBER).build();
        }

        if (myParticipantInfo.getConversation().getType() == ConversationType.DIRECT) {
            throw AppException.builder().appError(AppError.CANNOT_CHANGE_ROLE).build();
        }

        userParticipantInfo.setChatRole(dto.getChatRole());

        ParticipantInfo saved = participantInfoRepository.save(userParticipantInfo);
        return participantInfoMapper.entityToResponse(saved);
    }

    private ParticipantInfo findByUserIdAndConversationId(Long userId, Long conversationId) {
        return participantInfoRepository
                .findByUserIdAndConversation_Id(userId, conversationId)
                .orElseThrow(() -> AppException.builder().appError(AppError.PARTICIPANT_INFO_NOT_FOUND).build());
    }

}
