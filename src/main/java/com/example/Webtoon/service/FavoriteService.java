package com.example.Webtoon.service;

import com.example.Webtoon.model.CreatorNotification;
import com.example.Webtoon.model.Favorite;
import com.example.Webtoon.model.Series;
import com.example.Webtoon.model.User;
import com.example.Webtoon.repository.CreatorNotificationRepository;
import com.example.Webtoon.repository.FavoriteRepository;
import com.example.Webtoon.repository.SeriesRepository;
import com.example.Webtoon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;



@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private CreatorNotificationRepository creatorNotificationRepository;

    @Autowired
    private SeriesRepository seriesRepository;



//    public void save(Long userId, Long seriesId, String type) {
//
//        boolean exists = favoriteRepository
//                .existsByUserIdAndSeriesIdAndType(userId, seriesId, type);
//
//        if (!exists) {
//
//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            Series series = seriesRepository.findById(seriesId)
//                    .orElseThrow(() -> new RuntimeException("Series not found"));
//
//            Favorite fav = new Favorite();
//            fav.setUser(user);
//            fav.setSeries(series);
//            fav.setType(type);
//
//            favoriteRepository.save(fav);
//
//            // 🔥 NOW CREATE NOTIFICATION
//
//            Long creatorId = series.getCreator().getId(); // important
//
//            // avoid self notification
//            if (creatorId.equals(userId)) return;
//
//            String message;
//
//            if ("LIKE".equalsIgnoreCase(type)) {
//                message = user.getUsername() + " liked your comic " + series.getTitle();
//            } else {
//                message = user.getUsername() + " saved your comic " + series.getTitle();
//            }
//
//            CreatorNotification notification = new CreatorNotification(
//                    creatorId,
//                    message,
//                    java.time.LocalDateTime.now()
//            );
//
//            creatorNotificationRepository.save(notification); // ✅ THIS LINE SAVES
//        }
//    }
public void save(Long userId, Long seriesId, String type) {

    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Series series = seriesRepository.findById(seriesId)
            .orElseThrow(() -> new RuntimeException("Series not found"));

    boolean exists = favoriteRepository
            .existsByUserAndSeriesAndType(user, series, type);

    if (!exists) {

        Favorite fav = new Favorite();
        fav.setUser(user);
        fav.setSeries(series);
        fav.setType(type);

        favoriteRepository.save(fav);

        Long creatorId = series.getCreator().getId();

        if (creatorId.equals(userId)) return;

        String message;

        if ("LIKE".equalsIgnoreCase(type)) {
            message = user.getUsername() + " liked your comic " + series.getTitle();
        } else if ("SAVE".equalsIgnoreCase(type)) {
            message = user.getUsername() + " saved your comic " + series.getTitle();
        } else {
            message = user.getUsername() + " interacted with your comic " + series.getTitle();
        }

        creatorNotificationRepository.save(
                new CreatorNotification(creatorId, message, java.time.LocalDateTime.now())
        );
    }
}

//unfavorite

//    public void remove(Long userId, Long seriesId, String type) {
//
//        boolean exists = favoriteRepository
//                .existsByUserIdAndSeriesIdAndType(userId, seriesId, type);
//
//        if (exists) {
//            favoriteRepository.deleteByUserIdAndSeriesIdAndType(userId, seriesId, type);
//
//            // OPTIONAL: notify creator (not recommended usually)
//            // creators typically don’t get "unliked" notifications
//        }
//    }


    public void remove(Long userId, Long seriesId, String type) {
        Optional<Favorite> favOpt = favoriteRepository.findByUserIdAndSeriesIdAndType(userId, seriesId, type);

        favOpt.ifPresent(fav -> {
            favoriteRepository.delete(fav);

            // optional: remove notification
            Long creatorId = fav.getSeries().getCreator().getId();
            if (!creatorId.equals(userId)) {
                String message = type.equalsIgnoreCase("LIKE") ?
                        fav.getUser().getUsername() + " liked your comic " + fav.getSeries().getTitle() :
                        fav.getUser().getUsername() + " saved your comic " + fav.getSeries().getTitle();

                creatorNotificationRepository.findByCreatorIdOrderByDateDesc(creatorId).stream()
                        .filter(n -> n.getMessage().equals(message))
                        .findFirst()
                        .ifPresent(creatorNotificationRepository::delete);
            }
        });
    }
}
