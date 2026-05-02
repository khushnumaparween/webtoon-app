package com.example.Webtoon.controller;

import com.example.Webtoon.model.Panel;
import com.example.Webtoon.repository.PanelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/series")
public class PanelController {

    @Autowired
    private PanelRepository panelRepository;

    // ---------- Edit Panel Page ----------

    @GetMapping("/editPanel/{panelId}")
    public String editPanelPage(@PathVariable Long panelId, Model model) {
        Panel panel = panelRepository.findById(panelId).orElse(null);
        if (panel == null) return "redirect:/series";

        model.addAttribute("panel", panel);
        model.addAttribute("episode", panel.getEpisode());
        model.addAttribute("episodeId", panel.getEpisode().getId());// adding episode explicitly
        return "edit_panel";
    }

   //upload panels code is inside episodecontroller


    @PostMapping("/editPanel/{panelId}")
    public String updatePanel(@PathVariable Long panelId,
                              @RequestParam(value = "panelText", required = false) String panelText) {
        Panel panel = panelRepository.findById(panelId).orElse(null);
        if (panel == null) return "redirect:/series";

        panel.setPanelText(panelText);
        panelRepository.save(panel);

        // Using the episode relationship, not a separate field
        return "redirect:/reader/readEpisode/" + panel.getEpisode().getId();
    }


    // ---------- Delete Panel ----------
    @GetMapping("/deletePanel/{panelId}")
    public String deletePanel(@PathVariable Long panelId) {
        Panel panel = panelRepository.findById(panelId).orElse(null);
        if (panel != null) {
            Long episodeId = panel.getEpisode().getId(); // use the episode relationship
            panelRepository.delete(panel);
            return "redirect:/reader/readEpisode/" + episodeId;
        }
        return "redirect:/series";
    }
}