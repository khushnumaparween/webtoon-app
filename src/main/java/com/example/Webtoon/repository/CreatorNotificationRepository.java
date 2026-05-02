package com.example.Webtoon.repository;

import com.example.Webtoon.model.CreatorNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CreatorNotificationRepository extends JpaRepository<CreatorNotification, Long> {
    List<CreatorNotification> findByCreatorIdOrderByDateDesc(Long creatorId);
}
