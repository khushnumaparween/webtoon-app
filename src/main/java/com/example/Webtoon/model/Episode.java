package com.example.Webtoon.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private int episodeNumber;

    @ManyToOne
    @JoinColumn(name = "series_id")
    private Series series;

    @Column(columnDefinition = "TEXT")
    private String episodeText;

    @Transient
    private List<Panel> panels; // Panels loaded manually in controller
}