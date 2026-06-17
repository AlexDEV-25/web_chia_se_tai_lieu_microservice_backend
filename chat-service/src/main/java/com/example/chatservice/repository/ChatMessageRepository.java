package com.example.chatservice.repository;

import com.example.chatservice.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findAllByConversation_IdOrderByCreatedAtAsc(Long conversationId);
}
