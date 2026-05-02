//package com.example.Webtoon.model;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class Panel {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private Long episodeId;
//
//    private String imageUrl;
//
//    private int panelOrder;
//
//    @Column(columnDefinition = "TEXT")
//    private String panelText; // Add this to Panel
//}

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

    // Link to Episode entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id") // maps to the episode table
    private Episode episode;

    private String imageUrl;

    private int panelOrder;

    @Column(columnDefinition = "TEXT")
    private String panelText;
}