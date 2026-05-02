package com.example.Webtoon.model;

import jakarta.persistence.*;
        import java.time.LocalDateTime;

@Entity
public class CreatorNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long creatorId;        // the recipient creator's user ID
    private String message;        // e.g., "abc liked your xyz comic"
    private LocalDateTime date;    // timestamp of the notification

    public CreatorNotification() {}

    public CreatorNotification(Long creatorId, String message, LocalDateTime date) {
        this.creatorId = creatorId;
        this.message = message;
        this.date = date;
    }

    public Long getId() { return id; }

    public Long getCreatorId() { return creatorId; }
    public void setCreatorId(Long creatorId) { this.creatorId = creatorId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
