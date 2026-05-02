//package com.example.Webtoon.controller;
//
//import com.example.Webtoon.model.*;
//import com.example.Webtoon.repository.*;
//
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import org.springframework.web.multipart.MultipartFile;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Optional;
//
//@Controller
//@RequestMapping("/series")
//public class SeriesPageController {
//
//    @Autowired
//    private SeriesRepository seriesRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PanelRepository panelRepository;
//
//    @Autowired
//    private ReadingHistoryRepository historyRepository;
//
//    @Autowired
//    private EpisodeRepository episodeRepository;
//
//
//    @GetMapping
//    public String creatorDashboard(Model model, Authentication authentication) {
//
//        String email = authentication.getName();
//
//        User user = userRepository.findByEmail(email).orElse(null);
//
//        List<Series> seriesList = seriesRepository.findByCreatorId(user.getId());
//
//        model.addAttribute("seriesList", seriesList);
//
//        return "series";
//    }
//
//    @PostMapping("/new")
//    public String createSeries(@RequestParam String title,
//                               @RequestParam String description,
//                               @RequestParam String genre,
//                               @RequestParam("coverImage") MultipartFile coverImage,
//                               Authentication authentication) throws IOException {
//
//        String email = authentication.getName();
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
////        User user = userRepository.findByEmail(email).orElse(null);
//
//        String uploadDir = System.getProperty("user.dir") + "/uploads/";
//        String fileName = System.currentTimeMillis() + "_" + coverImage.getOriginalFilename();
////        String fileName = coverImage.getOriginalFilename();
//
//        Path uploadPath = Paths.get(uploadDir);
//
//        if (!Files.exists(uploadPath)) {
//            Files.createDirectories(uploadPath);
//        }
//
//        if (!coverImage.isEmpty()) {
//            Path filePath = uploadPath.resolve(fileName);
//            coverImage.transferTo(filePath.toFile());
//        }
//
//        Series series = new Series();
//        series.setTitle(title);
//        series.setDescription(description);
//        series.setGenre(genre);
//        series.setCoverImage("/uploads/" + fileName); // save path
//        series.setCreatorId(user.getId());
//
//        seriesRepository.save(series);
//
//        return "redirect:/series";
//    }
//
//    //EDIT BUTTON
//
//
//    @GetMapping("/edit/{id}")
//    public String editSeries(@PathVariable Long id, Model model) {
//
//        Series series = seriesRepository.findById(id).orElse(null);
//
//        model.addAttribute("series", series);
//
//        return "edit-series";
//    }
//
//
//    @PostMapping("/update/{id}")
//    public String updateSeries(@PathVariable Long id,
//                               @RequestParam String title,
//                               @RequestParam String description,
//                               @RequestParam String genre) {
//
////        Series series = seriesRepository.findById(id).orElse(null);
//        Series series = seriesRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Series not found"));
//
//        series.setTitle(title);
//        series.setDescription(description);
//        series.setGenre(genre);
//
//        seriesRepository.save(series);
//
//        return "redirect:/series";
//    }
//
//    //DELETE BUTTON
////
////    @PostMapping("/delete/{id}")
////    public String deleteSeries(@PathVariable Long id) {
////
////        seriesRepository.deleteById(id);
////
////        return "redirect:/series";
////    }
//
//
//    @PostMapping("/delete/{id}")
//    @Transactional
//    public String deleteSeries(@PathVariable Long id) {
//
//        Series series = seriesRepository.findById(id).orElse(null);
//
//        if (series != null) {
//            // Fetch episodes of the series using your repository
//            List<Episode> episodes = episodeRepository.findBySeriesOrderByEpisodeNumberAsc(series);
//
//            for (Episode ep : episodes) {
//                historyRepository.deleteAllByEpisodeId(ep.getId());
//                panelRepository.deleteAllByEpisodeId(ep.getId());
//            }
//
//            // Delete episodes
//            episodeRepository.deleteAll(episodes);
//
//            // Delete the series itself
//            seriesRepository.delete(series);
//        }
//
//        return "redirect:/series";
//    }
//
//    //profile endpoint for creator
//
//    @GetMapping("/creator/profile")
//    public String creatorProfile(Model model, Authentication authentication) {
//        if (authentication == null) return "redirect:/login";
//
//        String email = authentication.getName();
//        User user = userRepository.findByEmail(email).orElse(null);
//        if (user == null) return "redirect:/login";
//
//        model.addAttribute("user", user);
//        model.addAttribute("profileUpdated", getProfileUpdatedStatus(user));
//        return "creator_profile";
//    }
//
//
//
//
//    @GetMapping("/creator/profile/edit")
//    public String editCreatorProfile(Model model, Authentication authentication) {
//        if (authentication == null) return "redirect:/login";
//
//        String email = authentication.getName();
//        User user = userRepository.findByEmail(email).orElse(null);
//        if (user == null) return "redirect:/login";
//
//        model.addAttribute("user", user);
//        model.addAttribute("profileUpdated", false);
//        return "creator_profile";
//    }
//
//    @PostMapping("/creator/profile/update")
//    public String updateCreatorProfile(@RequestParam("name") String name,
//                                       @RequestParam("bio") String bio,
//                                       @RequestParam("image") MultipartFile image,
//                                       Authentication authentication) throws IOException {
//        String email = authentication.getName();
//        User user = userRepository.findByEmail(email).orElse(null);
//
//        if (user != null) {
//            user.setName(name);
//            user.setBio(bio);
//
//            if (image != null && !image.isEmpty()) {
//                String filename = image.getOriginalFilename();
//                Path path = Paths.get("uploads/" + filename);
//                Files.write(path, image.getBytes());
//                user.setImage("/uploads/" + filename);
//            }
//            userRepository.save(user);
//        }
//        return "redirect:/creator/profile";
//    }
//
//
//
//
//
//    private Boolean getProfileUpdatedStatus(User user) {
//        if (user == null) return false;
//
//        // Check if required fields are non-null and non-empty
//        boolean hasName = user.getName() != null && !user.getName().trim().isEmpty();
//        boolean hasBio = user.getBio() != null && !user.getBio().trim().isEmpty();
//        boolean hasImage = user.getImage() != null && !user.getImage().trim().isEmpty();
//
//        // Return true only if all required fields are filled
//        return hasName && hasBio && hasImage;
//    }
//
//
//
//    //edit and update
//    // Edit creator profile
//    @GetMapping("/creator/profile/edit")
//    public String editCreatorProfile(Model model, Authentication authentication) {
//        if (authentication == null) {
//            return "redirect:/login";
//        }
//
//        String email = authentication.getName();
//        User user = userRepository.findByEmail(email).orElse(null);
//
//        if (user == null) {
//            return "redirect:/login";
//        }
//
//        model.addAttribute("user", user);
//        model.addAttribute("profileUpdated", false); // show the edit form
//        return "creator_profile"; // your Thymeleaf page
//    }
//
//    // Update creator profile
//    @PostMapping("/creator/profile/update")
//    public String updateCreatorProfile(@RequestParam("name") String name,
//                                       @RequestParam("bio") String bio,
//                                       @RequestParam("image") MultipartFile image,
//                                       Authentication authentication) throws IOException {
//        String email = authentication.getName();
//        User user = userRepository.findByEmail(email).orElse(null);
//
//        if (user != null) {
//            user.setName(name);
//            user.setBio(bio);
//
//            if (image != null && !image.isEmpty()) {
//                String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
//                Path path = Paths.get("uploads/" + filename);
//                Files.write(path, image.getBytes());
//                user.setImage("/uploads/" + filename);
//            }
//
//            userRepository.save(user);
//        }
//
//        return "redirect:/series/creator/profile"; // redirect back to creator profile page
//    }
//
//
//}
//
//
//



package com.example.Webtoon.controller;

import com.example.Webtoon.model.*;
import com.example.Webtoon.repository.*;

import jakarta.transaction.Transactional;
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
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/series")
public class SeriesPageController {

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PanelRepository panelRepository;

    @Autowired
    private ReadingHistoryRepository historyRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private CreatorNotificationRepository creatorNotificationRepository;


    // ================= Series Dashboard =================
    @GetMapping
    public String creatorDashboard(Model model, Authentication authentication) {
        if (authentication == null) return "redirect:/login";

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return "redirect:/login";

        List<Series> seriesList = seriesRepository.findByCreatorId(user.getId());
        model.addAttribute("seriesList", seriesList);

        return "series";
    }

//    // Add new series
//    @PostMapping("/new")
//    public String createSeries(@RequestParam String title,
//                               @RequestParam String description,
//                               @RequestParam String genre,
//                               @RequestParam("coverImage") MultipartFile coverImage,
//                               Authentication authentication) throws IOException {
//
//        String email = authentication.getName();
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        String uploadDir = System.getProperty("user.dir") + "/uploads/";
//        String fileName = System.currentTimeMillis() + "_" + coverImage.getOriginalFilename();
//        Path uploadPath = Paths.get(uploadDir);
//
//        if (!Files.exists(uploadPath)) {
//            Files.createDirectories(uploadPath);
//        }
//
//        if (!coverImage.isEmpty()) {
//            Path filePath = uploadPath.resolve(fileName);
//            coverImage.transferTo(filePath.toFile());
//        }
//
//        Series series = new Series();
//        series.setTitle(title);
//        series.setDescription(description);
//        series.setGenre(genre);
//        series.setCoverImage("/uploads/" + fileName);
//        series.setCreatorId(user.getId());
//
//        seriesRepository.save(series);
//        return "redirect:/series";
//    }



    @PostMapping("/new")
    public String createSeries(@RequestParam String title,
                               @RequestParam String description,
                               @RequestParam String genre,
                               @RequestParam("coverImage") MultipartFile coverImage,
                               Authentication authentication) throws IOException {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        String fileName = System.currentTimeMillis() + "_" + coverImage.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        if (!coverImage.isEmpty()) {
            Path filePath = uploadPath.resolve(fileName);
            coverImage.transferTo(filePath.toFile());
        }

        Series series = new Series();
        series.setTitle(title);
        series.setDescription(description);
        series.setGenre(genre);
        series.setCoverImage("/uploads/" + fileName);
        series.setCreatorId(user.getId());

        // ✅ Save series
        seriesRepository.save(series);

        // ✅ CREATE NOTIFICATIONS FOR READERS
        List<User> readers = userRepository.findAll();

        for (User reader : readers) {
            // Don't notify the creator themselves
            if (!reader.getId().equals(user.getId())) {

                Notification notification = new Notification();
                notification.setMessage(user.getName() + " posted a new series: " + series.getTitle());
                notification.setDate(LocalDateTime.now());
                notification.setRecipientId(reader.getId());
                notification.setRecipientType("USER");
                notification.setCreatorId(user.getId());

                notificationRepository.save(notification);
            }
        }

        return "redirect:/series";
    }

    // Edit series
    @GetMapping("/edit/{id}")
    public String editSeries(@PathVariable Long id, Model model) {
        Series series = seriesRepository.findById(id).orElse(null);
        model.addAttribute("series", series);
        return "edit-series";
    }

    // Update series
    @PostMapping("/update/{id}")
    public String updateSeries(@PathVariable Long id,
                               @RequestParam String title,
                               @RequestParam String description,
                               @RequestParam String genre) {

        Series series = seriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Series not found"));

        series.setTitle(title);
        series.setDescription(description);
        series.setGenre(genre);
        seriesRepository.save(series);

        return "redirect:/series";
    }

    // Delete series
    @PostMapping("/delete/{id}")
    @Transactional
    public String deleteSeries(@PathVariable Long id) {
        Series series = seriesRepository.findById(id).orElse(null);

        if (series != null) {
            List<Episode> episodes = episodeRepository.findBySeriesOrderByEpisodeNumberAsc(series);

            for (Episode ep : episodes) {
                historyRepository.deleteAllByEpisodeId(ep.getId());
                panelRepository.deleteAllByEpisodeId(ep.getId());
            }

            episodeRepository.deleteAll(episodes);
            seriesRepository.delete(series);
        }

        return "redirect:/series";
    }

    // ================= Creator Profile =================

    @GetMapping("/creator/profile")
    public String creatorProfile(Model model, Authentication authentication) {
        if (authentication == null) return "redirect:/login";

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("profileUpdated", getProfileUpdatedStatus(user));
        return "creator_profile";
    }

    @GetMapping("/creator/profile/edit")
    public String editCreatorProfile(Model model, Authentication authentication) {
        if (authentication == null) return "redirect:/login";

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("profileUpdated", false);
        return "creator_profile";
    }

    @PostMapping("/creator/profile/update")
    public String updateCreatorProfile(@RequestParam("name") String name,
                                       @RequestParam("bio") String bio,
                                       @RequestParam("image") MultipartFile image,
                                       Authentication authentication) throws IOException {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            user.setName(name);
            user.setBio(bio);

            if (image != null && !image.isEmpty()) {
                String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path path = Paths.get("uploads/" + filename);
                Files.write(path, image.getBytes());
                user.setImage("/uploads/" + filename);
            }

            userRepository.save(user);
        }

        return "redirect:/series/creator/profile";
    }

    // Helper to check if profile is fully updated
    private Boolean getProfileUpdatedStatus(User user) {
        if (user == null) return false;

        boolean hasName = user.getName() != null && !user.getName().trim().isEmpty();
        boolean hasBio = user.getBio() != null && !user.getBio().trim().isEmpty();
        boolean hasImage = user.getImage() != null && !user.getImage().trim().isEmpty();

        return hasName && hasBio && hasImage;
    }


    // ================= Creator Notifications =================



    @GetMapping("/creator/creator_notification")
    public String creatorNotifications(Model model, Authentication authentication) {
        if (authentication == null) return "redirect:/login";

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return "redirect:/login";

        List<CreatorNotification> notifications = creatorNotificationRepository
                .findByCreatorIdOrderByDateDesc(user.getId());

        model.addAttribute("creatorNotifications", notifications);
        return "creator_notifications";
    }
}