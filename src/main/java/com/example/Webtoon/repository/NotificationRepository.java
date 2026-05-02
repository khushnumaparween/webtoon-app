//package com.example.Webtoon.repository;
//
//import com.example.Webtoon.model.Notification;
//import org.springframework.data.jpa.repository.JpaRepository;
//import java.util.List;
//
//public interface NotificationRepository extends JpaRepository<Notification, Long> {
//    List<Notification> findByRecipientIdAndRecipientTypeOrderByDateDesc(Long recipientId, String recipientType);
//}

//package com.example.Webtoon.repository;
//
//import com.example.Webtoon.model.Notification;
//import org.springframework.data.jpa.repository.JpaRepository;
//import java.util.List;
//
//public interface NotificationRepository extends JpaRepository<Notification, Long> {
//    List<Notification> findByCreatorIdOrderByDateDesc(Long creatorId);
//    List<Notification> findByReaderIdOrderByDateDesc(Long readerId);
//    List<Notification> findByRecipientIdAndRecipientTypeOrderByDateDesc(Long id, String reader);
//}
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