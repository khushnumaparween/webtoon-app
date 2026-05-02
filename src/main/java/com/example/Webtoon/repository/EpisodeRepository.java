package com.example.Webtoon.repository;

import com.example.Webtoon.model.Episode;
import com.example.Webtoon.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {

    // Find all episodes of a series, ordered by episodeNumber
    List<Episode> findBySeriesOrderByEpisodeNumberAsc(Series series);
}