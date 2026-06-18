package com.example.chatservice.repository;


import com.example.chatservice.model.ParticipantInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantInfoRepository extends JpaRepository<ParticipantInfo, Long> {
    boolean existsByConversation_IdAndUserId(Long conversationId, Long UserId);

    Optional<ParticipantInfo> findByConversation_IdAndUserId(Long conversationId, Long userId);

    Optional<ParticipantInfo> findByIdAndUserId(Long conversationId, Long userId);

    Optional<ParticipantInfo> findByUserIdAndConversation_Id(Long userId, Long conversationId);

}
