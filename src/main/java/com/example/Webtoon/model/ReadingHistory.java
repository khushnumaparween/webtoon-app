package com.example.Webtoon.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ReadingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long seriesId;

    @ManyToOne
    @JoinColumn(name = "episode_id")
    private Episode episode; // add this instead of Long episodeId

    private LocalDateTime lastRead;
}