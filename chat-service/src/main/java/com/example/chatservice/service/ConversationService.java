package com.example.chatservice.service;


import com.example.AppError;
import com.example.ChatRole;
import com.example.ConversationType;
import com.example.chatservice.dto.request.ConversationGroupRequest;
import com.example.chatservice.dto.request.ConversationRequest;
import com.example.chatservice.dto.response.ConversationResponse;
import com.example.chatservice.dto.response.ParticipantInfoResponse;
import com.example.chatservice.mapper.ConversationMapper;
import com.example.chatservice.mapper.ParticipantInfoMapper;
import com.example.chatservice.model.Conversation;
import com.example.chatservice.model.ParticipantInfo;
import com.example.chatservice.repository.ConversationRepository;
import com.example.chatservice.repository.ParticipantInfoRepository;
import com.example.chatservice.repository.httpclient.FileClient;
import com.example.chatservice.repository.httpclient.ProfileClient;
import com.example.commondto.response.UserDetailInfoResponse;
import com.example.commonexception.exception.AppException;
import com.example.commonsecurity.helper.GetUserIdByToken;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final ParticipantInfoRepository participantInfoRepository;
    private final ConversationMapper conversationMapper;
    private final ParticipantInfoMapper participantInfoMapper;
    private final FileClient fileClient;
    private final ProfileClient profileClient;

    @PreAuthorize("hasAuthority('GET_MY_CONVERSATION')")
    public List<ConversationResponse> getMyConversations() {
        Long userId = GetUserIdByToken.get();
        List<Conversation> conversations = conversationRepository.findMyConversations(userId);
        return conversations.stream()
                .map(conversation -> toConversationResponse(conversation, userId)).toList();
    }

    @Transactional
    @PreAuthorize("hasAuthority('CREATE_DIRECT_CONVERSATION')")
    public ConversationResponse createDirectConversation(ConversationRequest request) {
        Long userId = GetUserIdByToken.get();

        if (request.getOtherUserIds().size() != 1) {
            throw AppException.builder().appError(AppError.MEMBER_LIMIT_EXCEEDED).build();
        }

        Long partnerId = request.getOtherUserIds().getFirst();

        UserDetailInfoResponse user = profileClient.getUserDetail(userId).getResult();
        UserDetailInfoResponse partner = profileClient.getUserDetail(partnerId).getResult();

        List<Long> participantIds = new ArrayList<>(request.getOtherUserIds());
        participantIds.add(userId);

        Optional<Conversation> existing = conversationRepository.findExactConversation(participantIds, 2);
        if (existing.isPresent()) {
            return toConversationResponse(existing.get(), userId);
        }

        Conversation entity = Conversation.builder().type(request.getType()).createdAt(LocalDateTime.now()).build();
        List<ParticipantInfo> participants = buildListParticipantInfo(userId, user, partnerId, partner, entity);

        entity.setParticipantInfos(participants);
        Conversation conversation = conversationRepository.save(entity);
        return toConversationResponse(conversation, userId);
    }

    @Transactional
    @PreAuthorize("hasAuthority('CREATE_GROUP_CONVERSATION')")
    public ConversationResponse createGroupConversation(MultipartFile avt, ConversationGroupRequest request) {
        Long userId = GetUserIdByToken.get();

        // 1. validate
        if (request.getOtherUserIds() == null || request.getOtherUserIds().size() < 2) {
            throw AppException.builder().appError(AppError.MEMBER_COUNT_TOO_SMALL).build();
        }

        String groupAvt = avt != null ? saveGroupAvt(avt) : null;
        String groupName = request.getGroupName() != null ? request.getGroupName()
                : "Nhóm " + (request.getOtherUserIds().size() + 1) + " người";

        // 3. tạo conversation
        Conversation conversation = Conversation
                .builder().type(request.getType())
                .groupAvatarUrl(groupAvt)
                .groupName(groupName)
                .createdAt(LocalDateTime.now()).build();

        List<Long> otherUserIds = request.getOtherUserIds();

        List<ParticipantInfo> participants = new ArrayList<>();

        // thêm mình
        UserDetailInfoResponse user = profileClient.getUserDetail(userId).getResult();
        participants.add(ParticipantInfo
                .builder()
                .userId(userId)
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .chatRole(ChatRole.MANAGER)
                .conversation(conversation).build());

        // thêm các user khác
        for (Long otherUserId : otherUserIds) {
            UserDetailInfoResponse otherUser = profileClient.getUserDetail(otherUserId).getResult();
            participants.add(ParticipantInfo
                    .builder()
                    .userId(otherUserId)
                    .fullName(otherUser.getFullName())
                    .avatarUrl(otherUser.getAvatarUrl())
                    .chatRole(ChatRole.MEMBER)
                    .conversation(conversation).build());
        }

        conversation.setParticipantInfos(participants);

        // 4. save (cascade ALL → save luôn participant)
        conversationRepository.save(conversation);

        return toConversationResponse(conversation, userId);
    }

    @PreAuthorize("hasAuthority('SEARCH_CONVERSATION')")
    public List<ConversationResponse> search(String keyword) {
        Long userId = GetUserIdByToken.get();

        List<Conversation> conversations = conversationRepository.search(userId, keyword);
        return conversations.stream().map(conversation -> toConversationResponse(conversation, userId)).toList();
    }

    @PreAuthorize("hasAuthority('GET_DETAIL_CONVERSATION')")
    public ConversationResponse getDetailConversation(Long id) {
        Long userId = GetUserIdByToken.get();

        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> AppException.builder().appError(AppError.CONVERSATION_NOT_FOUND).build());

        if (!participantInfoRepository.existsByConversation_IdAndUserId(id, userId)) {
            throw AppException.builder().appError(AppError.NOT_CONVERSATION_MEMBER).build();
        }

        return toConversationResponse(conversation, userId);
    }

    private String saveGroupAvt(MultipartFile avt) {
        try {
            Map<String, Object> handleAvt = fileClient.uploadImage(avt).getResult();
            return (String) handleAvt.get("secure_url");
        } catch (Exception e) {
            throw AppException.builder().appError(AppError.UPDATE_PROFILE_FAILED).build();
        }
    }

    private List<ParticipantInfo> buildListParticipantInfo(Long userId, UserDetailInfoResponse user, Long partnerId, UserDetailInfoResponse partner, Conversation conversation) {
        List<ParticipantInfo> participants = new ArrayList<>();

        participants.add(ParticipantInfo
                .builder()
                .userId(userId)
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl()).build());

        participants.add(ParticipantInfo
                .builder().userId(partnerId)
                .fullName(partner.getFullName())
                .avatarUrl(partner.getAvatarUrl()).build());

        return participants;
    }

    private ConversationResponse toConversationResponse(Conversation conversation, Long userId) {
        ConversationResponse conversationResponse = conversationMapper.toConversationResponse(conversation);

        if (conversation.getType() == ConversationType.DIRECT) {
            ParticipantInfo partnerInfo = conversation.getParticipantInfos().stream()
                    .filter(p -> !p.getUserId().equals(userId))
                    .findFirst().orElseThrow(() -> AppException.builder().appError(AppError.PARTNER_NOT_FOUND).build());

            conversationResponse.setConversationAvatar(partnerInfo.getAvatarUrl());
            conversationResponse.setConversationName(partnerInfo.getFullName());
        } else {
            conversationResponse.setConversationAvatar(conversation.getGroupAvatarUrl());
            conversationResponse.setConversationName(conversation.getGroupName());
        }

        List<ParticipantInfoResponse> participantInfoResponse = conversation.getParticipantInfos().stream()
                .map(participantInfoMapper::entityToResponse).toList();
        conversationResponse.setParticipantInfos(participantInfoResponse);
        return conversationResponse;
    }

}
