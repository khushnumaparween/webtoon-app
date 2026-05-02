package com.example.Webtoon.controller;

import com.example.Webtoon.model.*;
import com.example.Webtoon.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/series")
public class EpisodeController {

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private PanelRepository panelRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    // ---------- Episode Page ----------
    @GetMapping("/addEpisode/{seriesId}")
    public String episodePage(@PathVariable Long seriesId, Model model) {
        Series series = seriesRepository.findById(seriesId).orElse(null);
        if (series == null) return "redirect:/series";

        // Fetch all episodes
        List<Episode> episodes = episodeRepository.findBySeriesOrderByEpisodeNumberAsc(series);

        // Attach panels to each episode
        for (Episode ep : episodes) {
            List<Panel> panels = panelRepository.findByEpisodeIdOrderByPanelOrderAsc(ep.getId());
            ep.setPanels(panels); // Episode class has transient List<Panel> panels
        }

        model.addAttribute("seriesId", seriesId);
        model.addAttribute("episodes", episodes);
        return "episodes"; // Thymeleaf page
    }

    // ---------- Add Episode ----------
    @PostMapping("/addEpisode/{seriesId}")
    public String addEpisode(@PathVariable Long seriesId,
                             @RequestParam String title,
                             @RequestParam int episodeNumber,
                             @RequestParam String episodeText) {

        Series series = seriesRepository.findById(seriesId).orElse(null);
        if (series == null) return "redirect:/series";

        Episode episode = new Episode();
        episode.setSeries(series);
        episode.setTitle(title);
        episode.setEpisodeNumber(episodeNumber);
        episode.setEpisodeText(episodeText);

        episodeRepository.save(episode);
        return "redirect:/series/addEpisode/" + seriesId;
    }

    // ---------- Edit Episode ----------
    @GetMapping("/editEpisode/{episodeId}")
    public String editEpisodePage(@PathVariable Long episodeId, Model model) {
        Episode episode = episodeRepository.findById(episodeId).orElse(null);
        if (episode == null) return "redirect:/series";
        model.addAttribute("episode", episode);
        return "edit_episode";
    }

    @PostMapping("/editEpisode/{episodeId}")
    public String updateEpisode(@PathVariable Long episodeId,
                                @RequestParam String title,
                                @RequestParam int episodeNumber,
                                @RequestParam String episodeText) {

        Episode episode = episodeRepository.findById(episodeId).orElse(null);
        if (episode == null) return "redirect:/series";

        episode.setTitle(title);
        episode.setEpisodeNumber(episodeNumber);
        episode.setEpisodeText(episodeText);
        episodeRepository.save(episode);

        return "redirect:/series/addEpisode/" + episode.getSeries().getId();
    }

    // ---------- Delete Episode ----------
//    @GetMapping("/deleteEpisode/{episodeId}")
//    public String deleteEpisode(@PathVariable Long episodeId) {
//        Episode episode = episodeRepository.findById(episodeId).orElse(null);
//        if (episode != null) {
//            panelRepository.deleteAllByEpisodeId(episodeId); // delete associated panels
//            episodeRepository.delete(episode);
//            return "redirect:/series/addEpisode/" + episode.getSeries().getId();
//        }
//        return "redirect:/series";
//    }

    @Autowired
    private ReadingHistoryRepository readingHistoryRepository; // add this

    @GetMapping("/deleteEpisode/{episodeId}")
    public String deleteEpisode(@PathVariable Long episodeId) {
        Episode episode = episodeRepository.findById(episodeId).orElse(null);
        if (episode != null) {
            // Delete all panels linked to this episode
            panelRepository.deleteAllByEpisodeId(episodeId);

            // Delete all reading history linked to this episode
            readingHistoryRepository.deleteAllByEpisodeId(episodeId);

            // Now delete the episode itself
            episodeRepository.delete(episode);

            return "redirect:/series/addEpisode/" + episode.getSeries().getId();
        }
        return "redirect:/series";
    }





    // ---------- Upload Panels ----------

    //get method
    @GetMapping("/uploadPanels/{episodeId}")
    public String uploadPanelPage(@PathVariable Long episodeId, Model model) {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new RuntimeException("Invalid episode ID"));
        model.addAttribute("episodeId", episodeId);
        model.addAttribute("seriesId", episode.getSeries().getId());
        return "upload_panels";
    }




    @PostMapping("/uploadPanels/{episodeId}")
    public String uploadPanels(@PathVariable Long episodeId,
                               @RequestParam("images") MultipartFile[] images,
                               @RequestParam(value = "panelText", required = false) String panelText) throws IOException {

        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new RuntimeException("Invalid episode ID"));

        int order = (int) panelRepository.countByEpisodeId(episodeId) + 1; // you can keep this if your repository method counts by episodeId
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        for (MultipartFile file : images) {
            if (!file.isEmpty()) {
                String originalName = Paths.get(file.getOriginalFilename()).getFileName().toString();
                File destination = new File(uploadDir + originalName);
                file.transferTo(destination);

                Panel panel = new Panel();
                panel.setEpisode(episode); // use the Episode entity instead of episodeId
                panel.setImageUrl("/uploads/" + originalName);
                panel.setPanelOrder(order++);
                panel.setPanelText(panelText);
                panelRepository.save(panel);
            }
        }

        return "redirect:/reader/readEpisode/" + episodeId;
    }
}