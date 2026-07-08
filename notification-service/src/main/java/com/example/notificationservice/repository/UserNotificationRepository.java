package com.example.notificationservice.repository;


import com.example.notificationservice.model.UserNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {

    Page<UserNotification> findByReceiverIdAndReadFalse(Long receiverId, Pageable pageable);
    
    List<UserNotification> findByReceiverIdAndReadFalse(Long receiverId);

    Optional<UserNotification> findByIdAndReceiverIdAndReadFalse(Long Id, Long receiverId);

    Page<UserNotification> findByReceiverId(Long receiverId, Pageable pageable);
}
