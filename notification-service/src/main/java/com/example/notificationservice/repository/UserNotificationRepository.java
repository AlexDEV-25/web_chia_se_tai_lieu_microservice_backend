package com.example.notificationservice.repository;


import com.example.notificationservice.model.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {

    List<UserNotification> findByReceiverIdAndReadFalse(Long receiverId);

    Optional<UserNotification> findByIdAndReceiverIdAndReadFalse(Long Id, Long receiverId);

    List<UserNotification> findByReceiverId(Long receiverId);
}
