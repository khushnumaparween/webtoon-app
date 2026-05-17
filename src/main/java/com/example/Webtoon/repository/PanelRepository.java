package com.example.Webtoon.repository;

import com.example.Webtoon.model.Panel;
import com.example.Webtoon.model.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PanelRepository extends JpaRepository<Panel, Long> {

    // Fetch panels by Episode entity
    List<Panel> findByEpisodeOrderByPanelOrderAsc(Episode episode);

    // Fetch panels by Episode ID
    List<Panel> findByEpisodeIdOrderByPanelOrderAsc(Long episodeId);

    // Old method you had
    List<Panel> findByEpisodeIdOrderByPanelOrder(Long episodeId);

    // Count panels for a specific episode
    long countByEpisodeId(Long episodeId);

    // Delete all panels for a specific episode
    @Modifying
    @Transactional
    @Query("DELETE FROM Panel p WHERE p.episode.id = :episodeId")
    void deleteAllByEpisodeId(Long episodeId);
}