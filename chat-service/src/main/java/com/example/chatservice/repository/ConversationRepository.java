package com.example.chatservice.repository;


import com.example.chatservice.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("""
                SELECT DISTINCT c FROM Conversation c
                JOIN c.participantInfos p
                WHERE p.userId = :userId
            """)
    List<Conversation> findMyConversations(Long userId);

    @Query(value = """
                SELECT c.* FROM conversation c
                JOIN participant_info p ON c.id = p.conversation_id
                GROUP BY c.id
                HAVING COUNT(DISTINCT p.userId) = :size
                   AND COUNT(DISTINCT CASE WHEN p.userId IN :userIds THEN p.userId END) = :size
            """, nativeQuery = true)
    Optional<Conversation> findExactConversation(@Param("userIds") List<Long> userIds, @Param("size") long size);

    @Query("""
              SELECT DISTINCT c
              FROM Conversation c
              JOIN c.participantInfos myP
              LEFT JOIN c.participantInfos otherP
              WHERE myP.userId = :myId
                AND (
                      c.groupName LIKE CONCAT('%', :keyword, '%')
                      OR
                      otherP.fullName LIKE CONCAT('%', :keyword, '%')
                	)
            """)
    List<Conversation> search(Long myId, String keyword);

}
