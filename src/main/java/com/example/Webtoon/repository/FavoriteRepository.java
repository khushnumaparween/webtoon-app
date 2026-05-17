package com.example.Webtoon.repository;

import com.example.Webtoon.model.Favorite;
import com.example.Webtoon.model.Series;
import com.example.Webtoon.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // Check if a favorite exists (like or save)
    boolean existsByUserAndSeriesAndType(User user, Series series, String type);

    // Get list of favorites by user and type
    List<Favorite> findByUserAndType(User user, String type);

    // Delete a favorite by user, series, and type
    void deleteByUserIdAndSeriesIdAndType(Long userId, Long seriesId, String type);

    // Optional to fetch a specific favorite
    Optional<Favorite> findByUserIdAndSeriesIdAndType(Long userId, Long seriesId, String type);

    // Convenience methods for unsave/remove saved series
    default void removeSaved(User user, Series series) {
        deleteByUserIdAndSeriesIdAndType(user.getId(), series.getId(), "SAVE");
    }

    default void removeLiked(User user, Series series) {
        deleteByUserIdAndSeriesIdAndType(user.getId(), series.getId(), "LIKE");
    }
}
