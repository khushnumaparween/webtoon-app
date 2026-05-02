//package com.example.Webtoon.model;
//
//import java.time.LocalDateTime;
//
//public class Notification {
//    private String message;
//    private LocalDateTime date;
//
//    public Notification(String message, LocalDateTime date) {
//        this.message = message;
//        this.date = date;
//    }
//
//    public String getMessage() { return message; }
//    public void setMessage(String message) { this.message = message; }
//
//    public LocalDateTime getDate() { return date; }
//    public void setDate(LocalDateTime date) { this.date = date; }
//}

package com.example.Webtoon.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(name = "reader_id")
    private Long recipientId; // This matches the repository query

    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "recipient_type")
    private String recipientType;

    // getter and setter
    public String getRecipientType() { return recipientType; }
    public void setRecipientType(String recipientType) { this.recipientType = recipientType; }

    public Notification() {}

    public Notification(String message, LocalDateTime date, Long recipientId, Long creatorId) {
        this.message = message;
        this.date = date;
        this.recipientId = recipientId;
        this.creatorId = creatorId;
    }

    // Getters and setters
    public Long getId() { return id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public Long getRecipientId() { return recipientId; }
    public void setRecipientId(Long recipientId) { this.recipientId = recipientId; }

    public Long getCreatorId() { return creatorId; }
    public void setCreatorId(Long creatorId) { this.creatorId = creatorId; }
}