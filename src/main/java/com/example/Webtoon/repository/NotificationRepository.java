package com.example.Webtoon.repository;

import com.example.Webtoon.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Fetch notifications for a specific recipient and type, newest first
    List<Notification> findByRecipientIdAndRecipientTypeOrderByDateDesc(Long recipientId, String recipientType);

    // Optional: fetch recent notifications globally
    List<Notification> findTop5ByOrderByDateDesc();
}