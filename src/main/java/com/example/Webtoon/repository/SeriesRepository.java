package com.example.Webtoon.repository;

import com.example.Webtoon.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface SeriesRepository extends JpaRepository<Series, Long> {

  List<Series> findByCreatorId(Long creatorId);

  List<Series> findAllByOrderByIdDesc();

  // latest 5 series
  List<Series> findTop5ByOrderByIdDesc();

  // same genre recommendation
  List<Series> findTop5ByGenre(String genre);

  List<Series> findByGenre(String genre);

  List<Series> findByGenreIgnoreCase(String genre);


//for search function

  @Query("SELECT s FROM Series s WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(s.creator.username) LIKE LOWER(CONCAT('%', :query, '%'))")
  List<Series> searchComics(@Param("query") String query);


//for genre wise display
  @Query("SELECT DISTINCT s.genre FROM Series s")
  List<String> findAllGenres();
}
