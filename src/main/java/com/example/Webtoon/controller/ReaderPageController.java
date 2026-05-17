//package com.example.Webtoon.controller;
//
//import com.example.Webtoon.model.*;
//import com.example.Webtoon.repository.EpisodeRepository;
//import com.example.Webtoon.repository.ReadingHistoryRepository;
//import com.example.Webtoon.repository.SeriesRepository;
//import com.example.Webtoon.repository.UserRepository;
//import com.example.Webtoon.service.SeriesService;
////import jakarta.persistence.criteria.Path;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
//@Controller
//public class ReaderPageController {
//
//    @Autowired
//    private SeriesRepository seriesRepository;
//
//
//    @Autowired
//    private ReadingHistoryRepository historyRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private EpisodeRepository episodeRepository;
//
//
//    @Autowired
//    private SeriesService seriesService;
//
//
//
//
//    @GetMapping("/reader_home")
//    public String readerHome(@RequestParam(required = false) String genre, Model model, Authentication authentication) {
//
//        User user = null;
//        if (authentication != null) {
//            String email = authentication.getName(); // or username if you prefer
//            user = userRepository.findByEmail(email).orElse(null);
//            model.addAttribute("user", user);
//        }
//
//        // All series
//        List<Series> seriesList = seriesRepository.findAll();
//
//        // Featured → latest 5
//        List<Series> featuredList = seriesRepository.findTop5ByOrderByIdDesc();
//
//        // Continue reading
//        List<Series> continueList = new ArrayList<>();
//        if (user != null) {
//            List<ReadingHistory> history =
//                    historyRepository.findTop5ByUserIdOrderByLastReadDesc(user.getId());
//            for (ReadingHistory h : history) {
//                seriesRepository.findById(h.getSeriesId()).ifPresent(continueList::add);
//            }
//        }
//
//        // Recommendations
//        List<Series> recommendations = new ArrayList<>();
//        if (!continueList.isEmpty()) {
//            String userGenre = continueList.get(0).getGenre();
//            recommendations = seriesRepository.findTop5ByGenre(userGenre);
//        }
//
//        // Genres for the tabs
//        List<String> genres = seriesRepository.findAllGenres();
//
//        model.addAttribute("seriesList", seriesList);
//        model.addAttribute("featuredList", featuredList);
//        model.addAttribute("continueList", continueList);
//        model.addAttribute("recommendations", recommendations);
//        model.addAttribute("genres", genres);
//
//        // If genre filter is applied
//        if (genre != null && !genre.isEmpty()) {
//            List<Series> genreSeries = seriesRepository.findByGenreIgnoreCase(genre);
//            model.addAttribute("seriesList", genreSeries); // overwrite to show filtered
//            model.addAttribute("selectedGenre", genre);
//            model.addAttribute("isFiltered", true);
//        } else {
//            model.addAttribute("isFiltered", false);
//        }
//
//        return "reader_home";
//    }
//
//
//
////    @GetMapping("/reader_home")
////    public String readerHome(@RequestParam(required = false) String genre, Model model, Authentication authentication) {
////
////        // Get logged-in user
////        String email = authentication.getName();
////        User user = userRepository.findByEmail(email).orElse(null);
////
////        // All series
////        List<Series> seriesList = seriesRepository.findAll();
////
////        // Featured → latest 5
////        List<Series> featuredList = seriesRepository.findTop5ByOrderByIdDesc();
////
////        // Continue reading
////        List<Series> continueList = new ArrayList<>();
////        List<ReadingHistory> history =
////                historyRepository.findTop5ByUserIdOrderByLastReadDesc(user.getId());
////        for (ReadingHistory h : history) {
////            seriesRepository.findById(h.getSeriesId()).ifPresent(continueList::add);
////        }
////
////        // Recommendations
////        List<Series> recommendations = new ArrayList<>();
////        if (!continueList.isEmpty()) {
////            String userGenre = continueList.get(0).getGenre();
////            recommendations = seriesRepository.findTop5ByGenre(userGenre);
////        }
////
////        // Genres for the tabs
////        List<String> genres = seriesRepository.findAllGenres();
////
////        model.addAttribute("seriesList", seriesList);
////        model.addAttribute("featuredList", featuredList);
////        model.addAttribute("continueList", continueList);
////        model.addAttribute("recommendations", recommendations);
////        model.addAttribute("genres", genres);
////
////        // If genre filter is applied
////        if (genre != null && !genre.isEmpty()) {
////            List<Series> genreSeries = seriesRepository.findByGenreIgnoreCase(genre);
////            model.addAttribute("seriesList", genreSeries); // overwrite to show filtered
////            model.addAttribute("selectedGenre", genre);
////            model.addAttribute("isFiltered", true);
////        } else {
////            model.addAttribute("isFiltered", false);
////        }
////
////        return "reader_home";
////    }
//
//
//
////series
//
//    @GetMapping("/series/{seriesId}")
//    public String viewSeries(@PathVariable Long seriesId, Model model) {
//
//        Series series = seriesRepository.findById(seriesId)
//                .orElseThrow(() -> new RuntimeException("Series not found"));
//
//        List<Episode> episodes =
//                episodeRepository.findBySeriesOrderByEpisodeNumberAsc(series);
//
//        model.addAttribute("series", series);
//        model.addAttribute("episodes", episodes);
//
//        return "read_series";
//    }
//
//// for search bar
//
//    @GetMapping("/search")
//    public String search(@RequestParam("query") String query, Model model) {
//
//        List<Series> results = seriesService.search(query);
//
//        model.addAttribute("results", results);
//        model.addAttribute("query", query);
//
//        return "search_results";
//    }
//
//
//
//
//    //ADDING MODEL IN PROFILE FILE
//    @GetMapping("/profile")
//    public String profilePage(Model model, Authentication authentication) {
//        if (authentication == null) {
//            return "redirect:/login"; // redirect unauthenticated users
//        }
//
//        String email = authentication.getName(); // get logged-in email
//        User user = userRepository.findByEmail(email).orElse(null);
//
//        if (user == null) {
//            return "redirect:/login"; // fallback if user not found in DB
//        }
//
//        // Null-safe profileUpdated
//        Boolean profileUpdated = getProfileUpdatedStatus(user); // implement this method
//        if (profileUpdated == null) {
//            profileUpdated = false; // default to false if null
//        }
//
//        model.addAttribute("user", user); // pass user object to Thymeleaf
//        model.addAttribute("profileUpdated", profileUpdated); // pass profileUpdated
//        return "profile"; // render profile.html
//    }
////    @GetMapping("/profile")
////    public String profilePage(Model model, Authentication authentication) {
////        if (authentication == null) {
////            return "redirect:/login"; // redirect unauthenticated users
////        }
////
////        String email = authentication.getName(); // get logged-in email
////        User user = userRepository.findByEmail(email).orElse(null);
////
////        if (user == null) {
////            return "redirect:/login"; // fallback if user not found in DB
////        }
////
////        model.addAttribute("user", user); // pass the user object to Thymeleaf
////        return "profile"; // render profile.html
////    }
//
//
//
//   //PROFILE ENDPOINT
//
//
//
//
//
//
//    @PostMapping("/profile/update")
//   public String updateProfile(@RequestParam("name") String name,
//                               @RequestParam("bio") String bio,
//                               @RequestParam("image") MultipartFile image,
//                               Authentication authentication) throws IOException {
//       String email = authentication.getName();
//       User user = userRepository.findByEmail(email).orElse(null);
//
//       if (user != null) {
//           user.setName(name);
//           user.setBio(bio);
//
//           if (image != null && !image.isEmpty()) {
//               String filename = image.getOriginalFilename();
//               Path path = Paths.get("uploads/" + filename); // make sure 'uploads' folder exists
//               Files.write(path, image.getBytes());
//               user.setImage("/uploads/" + filename);
//           }
//
//           userRepository.save(user);
//       }
//
//       return "redirect:/profile";
//   }
//
//
//
//
//    // NOTIFICATION ENDPOINT
//
//    @GetMapping("/notifications")
//    public String showNotifications(Model model, Authentication authentication) {
//        if (authentication == null) {
//            return "redirect:/login"; // redirect unauthenticated users
//        }
//
//        String email = authentication.getName();
//        User user = userRepository.findByEmail(email).orElse(null);
//
//        if (user == null) {
//            return "redirect:/login"; // fallback if user not found
//        }
//
//        List<Notification> notifications = new ArrayList<>();
//
//        // Add recently read comics
//        historyRepository.findTop5ByUserIdOrderByLastReadDesc(user.getId())
//                .forEach(h -> {
//                    String msg = "You read: " + seriesRepository.findById(h.getSeriesId())
//                            .map(Series::getTitle)
//                            .orElse("Unknown");
//                    notifications.add(new Notification(msg, h.getLastRead()));
//                });
//
//        // Add new arrivals
//        seriesRepository.findTop5ByOrderByIdDesc()
//                .forEach(s -> notifications.add(new Notification(
//                        "New arrival: " + s.getTitle(),
//                        LocalDateTime.now() // you can customize date if you have it in Series
//                )));
//
//        model.addAttribute("notifications", notifications);
//        return "notifications"; // Thymeleaf template notifications.html
//    }
//
//}


package com.example.Webtoon.controller;

import com.example.Webtoon.model.*;
import com.example.Webtoon.repository.*;
import com.example.Webtoon.service.FavoriteService;
import com.example.Webtoon.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ReaderPageController {

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private ReadingHistoryRepository historyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private SeriesService seriesService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private FavoriteRepository favoriteRepository;

    // Reader home page
    @GetMapping("/reader_home")
    public String readerHome(@RequestParam(required = false) String genre, Model model, Authentication authentication) {
        User user = null;
        if (authentication != null) {
            String email = authentication.getName();
            user = userRepository.findByEmail(email).orElse(null);
            model.addAttribute("user", user);
        }

        List<Series> seriesList = seriesRepository.findAll();
        List<Series> featuredList = seriesRepository.findTop5ByOrderByIdDesc();
        List<Series> continueList = new ArrayList<>();

        if (user != null) {
            List<ReadingHistory> history = historyRepository.findTop5ByUserIdOrderByLastReadDesc(user.getId());
            for (ReadingHistory h : history) {
                seriesRepository.findById(h.getSeriesId()).ifPresent(continueList::add);
            }
        }

        List<Series> recommendations = new ArrayList<>();
        if (!continueList.isEmpty()) {
            String userGenre = continueList.get(0).getGenre();
            recommendations = seriesRepository.findTop5ByGenre(userGenre);
        }

        List<String> genres = seriesRepository.findAllGenres();

        model.addAttribute("seriesList", seriesList);
        model.addAttribute("featuredList", featuredList);
        model.addAttribute("continueList", continueList);
        model.addAttribute("recommendations", recommendations);
        model.addAttribute("genres", genres);

        if (genre != null && !genre.isEmpty()) {
            List<Series> genreSeries = seriesRepository.findByGenreIgnoreCase(genre);
            model.addAttribute("seriesList", genreSeries);
            model.addAttribute("selectedGenre", genre);
            model.addAttribute("isFiltered", true);
        } else {
            model.addAttribute("isFiltered", false);
        }

        return "reader_home";
    }

    // View single series
    @GetMapping("/series/{seriesId}")
    public String viewSeries(@PathVariable Long seriesId, Model model) {
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new RuntimeException("Series not found"));
        List<Episode> episodes = episodeRepository.findBySeriesOrderByEpisodeNumberAsc(series);

        model.addAttribute("series", series);
        model.addAttribute("episodes", episodes);

        return "read_series";
    }

    // Search series
    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        List<Series> results = seriesService.search(query);
        model.addAttribute("results", results);
        model.addAttribute("query", query);
        return "search_results";
    }

    // Profile page
    @GetMapping("/profile")
    public String profilePage(Model model, Authentication authentication) {
        if (authentication == null) return "redirect:/login";

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return "redirect:/login";

        Boolean profileUpdated = getProfileUpdatedStatus(user);

        model.addAttribute("user", user);
        model.addAttribute("profileUpdated", profileUpdated != null ? profileUpdated : false);
        return "profile";
    }

    // Update profile
    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam("name") String name,
                                @RequestParam("bio") String bio,
                                @RequestParam("image") MultipartFile image,
                                Authentication authentication) throws IOException {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            user.setName(name);
            user.setBio(bio);

            if (image != null && !image.isEmpty()) {
                String filename = image.getOriginalFilename();
                Path path = Paths.get("uploads/" + filename);
                Files.write(path, image.getBytes());
                user.setImage("/uploads/" + filename);
            }

            userRepository.save(user);
        }

        return "redirect:/profile";
    }


    // Check if profile is updated
    private Boolean getProfileUpdatedStatus(User user) {
        if (user == null) return false;

        boolean hasName = user.getName() != null && !user.getName().isEmpty();
        boolean hasBio = user.getBio() != null && !user.getBio().isEmpty();
        boolean hasImage = user.getImage() != null && !user.getImage().isEmpty();

        return hasName && hasBio && hasImage;
    }


    //edit profile

    @GetMapping("/profile/edit")
    public String editProfile(Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("profileUpdated", false); // allow form to show
        return "profile"; // reuse profile.html
    }


    // ================= Reader Notifications =================
    @GetMapping("/notifications")
    public String showNotifications(Model model, Authentication authentication) {
        if (authentication == null) return "redirect:/login";

        // Get logged-in user's email
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return "redirect:/login";

        // Fetch notifications for this user
        List<Notification> notifications = notificationRepository
                .findByRecipientIdAndRecipientTypeOrderByDateDesc(user.getId(), "USER");

        // Optional debug: print if no notifications found
        if (notifications.isEmpty()) {
            System.out.println("No notifications found for user " + user.getId());
        }

        // Add notifications to the model
        model.addAttribute("notifications", notifications);

        return "notifications"; // Thymeleaf template name
    }

//favorites and saved

    @GetMapping("/favorites")
    public String getFavorites(Model model, Principal principal) {

        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Favorite> liked = favoriteRepository.findByUserAndType(user, "LIKE");
        List<Favorite> saved = favoriteRepository.findByUserAndType(user, "BOOKMARK");

        model.addAttribute("likedList", liked);
        model.addAttribute("savedList", saved);

        return "favorites"; // favorites.html
    }

    @PostMapping("/series/like/{id}")
    public String likeSeries(@PathVariable Long id, Authentication auth) {
        User user = getUser(auth);
        favoriteService.save(user.getId(), id, "LIKE");
        return "redirect:/favorites";
    }

    @PostMapping("/series/bookmark/{id}")
    public String bookmarkSeries(@PathVariable Long id, Authentication auth) {
        User user = getUser(auth);
        favoriteService.save(user.getId(), id, "BOOKMARK");
        return "redirect:/favorites";
    }

    private User getUser(Authentication auth) {
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    //UNFAV AND UNSAVE

    @PostMapping("/favorite/remove")
    public String removeFavorite(@RequestParam Long seriesId,
                                 @RequestParam String type,
                                 Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Map form type to DB type
        String dbType = "SAVE".equalsIgnoreCase(type) ? "BOOKMARK" : type.toUpperCase();

        favoriteService.remove(user.getId(), seriesId, dbType);

        // Always redirect to the favorites page
        return "redirect:/favorites";
    }
}

