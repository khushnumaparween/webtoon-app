package com.example.Webtoon.controller;

import com.example.Webtoon.model.*;
import com.example.Webtoon.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class WebtoonControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private PanelRepository panelRepository;

    private User testUser;

    @BeforeEach
    void setupUser() {
        // Create a user for authentication
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password"); // store as plain text for tests
        userRepository.save(testUser);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testCreateSeriesAndDelete() throws Exception {
        // Create a new series
        mockMvc.perform(multipart("/series/new")
                        .file(new MockMultipartFile("coverImage", "cover.jpg", MediaType.IMAGE_JPEG_VALUE,
                                "dummy".getBytes(StandardCharsets.UTF_8)))
                        .param("title", "Test Series")
                        .param("description", "Test Desc")
                        .param("genre", "Comedy"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/series"));

        Series series = seriesRepository.findByCreatorId(testUser.getId()).get(0);
        assertThat(series.getTitle()).isEqualTo("Test Series");

        // Delete series
        mockMvc.perform(post("/series/delete/" + series.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/series"));

        assertThat(seriesRepository.findById(series.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testAddEditDeleteEpisode() throws Exception {
        Series series = new Series();
        series.setTitle("Series 1");
        series.setCreatorId(testUser.getId());
        seriesRepository.save(series);

        // Add episode
        mockMvc.perform(post("/series/addEpisode/" + series.getId())
                        .param("title", "Episode 1")
                        .param("episodeNumber", "1")
                        .param("episodeText", "Hello World"))
                .andExpect(status().is3xxRedirection());

        Episode episode = episodeRepository.findBySeriesOrderByEpisodeNumberAsc(series).get(0);
        assertThat(episode.getTitle()).isEqualTo("Episode 1");

        // Edit episode
        mockMvc.perform(post("/series/editEpisode/" + episode.getId())
                        .param("title", "Episode 1 Edited")
                        .param("episodeNumber", "1")
                        .param("episodeText", "Updated Text"))
                .andExpect(status().is3xxRedirection());

        Episode updated = episodeRepository.findById(episode.getId()).get();
        assertThat(updated.getTitle()).isEqualTo("Episode 1 Edited");

        // Delete episode
        mockMvc.perform(get("/series/deleteEpisode/" + episode.getId()))
                .andExpect(status().is3xxRedirection());

        assertThat(episodeRepository.findById(episode.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testUploadAndEditDeletePanel() throws Exception {
        Series series = new Series();
        series.setTitle("Series Panel");
        series.setCreatorId(testUser.getId());
        seriesRepository.save(series);

        Episode episode = new Episode();
        episode.setTitle("Ep Panel");
        episode.setSeries(series);
        episodeRepository.save(episode);

        // Upload panel
        MockMultipartFile panelFile = new MockMultipartFile(
                "images", "panel.jpg", MediaType.IMAGE_JPEG_VALUE, "dummy".getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/series/uploadPanels/" + episode.getId())
                        .file(panelFile)
                        .param("panelText", "Panel Text"))
                .andExpect(status().is3xxRedirection());

        Panel panel = panelRepository.findByEpisodeIdOrderByPanelOrderAsc(episode.getId()).get(0);
        assertThat(panel.getPanelText()).isEqualTo("Panel Text");

        // Edit panel
        mockMvc.perform(post("/series/editPanel/" + panel.getId())
                        .param("panelText", "Updated Panel"))
                .andExpect(status().is3xxRedirection());

        Panel updatedPanel = panelRepository.findById(panel.getId()).get();
        assertThat(updatedPanel.getPanelText()).isEqualTo("Updated Panel");

        // Delete panel
        mockMvc.perform(get("/series/deletePanel/" + panel.getId()))
                .andExpect(status().is3xxRedirection());

        assertThat(panelRepository.findById(panel.getId())).isEmpty();
    }
}