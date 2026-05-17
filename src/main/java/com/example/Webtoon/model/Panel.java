package com.example.Webtoon.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Panel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Linking to Episode entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id") // maps to the episode table
    private Episode episode;

    private String imageUrl;

    private int panelOrder;

    @Column(columnDefinition = "TEXT")
    private String panelText;
}