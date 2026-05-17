package com.example.Webtoon.controller;

import com.example.Webtoon.model.*;
import com.example.Webtoon.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reader")
public class ReaderSideController {

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private PanelRepository panelRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReadingHistoryRepository historyRepository;

    @GetMapping("/readEpisode/{episodeId}")
    public String readEpisode(@PathVariable Long episodeId,
                              Model model,
                              Authentication authentication) {

        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new RuntimeException("Episode not found"));

        Series series = episode.getSeries();

        // Use Episode entity for repository query
        List<Panel> panels = panelRepository.findByEpisodeOrderByPanelOrderAsc(episode);

        List<Episode> episodes = episodeRepository.findBySeriesOrderByEpisodeNumberAsc(series);

        model.addAttribute("series", series);
        model.addAttribute("episodes", episodes);
        model.addAttribute("panels", panels);
        model.addAttribute("episodeTitle", episode.getTitle());
        model.addAttribute("episodeText", episode.getEpisodeText());

        if (authentication != null) {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Long seriesId = series.getId();

            Optional<ReadingHistory> existing =
                    historyRepository.findByUserIdAndSeriesId(user.getId(), seriesId);

            ReadingHistory history;

            if (existing.isPresent()) {
                history = existing.get();
                history.setEpisode(episode);
                history.setLastRead(LocalDateTime.now());
            } else {
                history = new ReadingHistory();
                history.setUserId(user.getId());
                history.setSeriesId(seriesId);
                history.setEpisode(episode);
                history.setLastRead(LocalDateTime.now());
            }

            historyRepository.save(history);
        }

        return "read_series";
    }


    @GetMapping("/back")
    public String backPage(@RequestParam Long seriesId, Authentication auth) {
        boolean isCreator = auth.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CREATOR"));

        if (isCreator) {
            return "redirect:/series/addEpisode/" + seriesId;
        } else {
            return "redirect:/reader_home";
        }
    }
}


