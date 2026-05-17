package com.example.Webtoon.repository;

import com.example.Webtoon.model.ReadingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ReadingHistoryRepository extends JpaRepository<ReadingHistory, Long> {

    List<ReadingHistory> findTop5ByUserIdOrderByLastReadDesc(Long userId);

    Optional<ReadingHistory> findByUserIdAndSeriesId(Long userId, Long seriesId);

    // ---------- Delete by episode ----------
    @Modifying
    @Transactional
    void deleteAllByEpisodeId(Long episodeId);
}
