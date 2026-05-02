package com.example.Webtoon.controller;

import com.example.Webtoon.model.*;
import com.example.Webtoon.repository.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReaderSideController.class)
@AutoConfigureMockMvc
class ReaderSideControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EpisodeRepository episodeRepository;

    @MockBean
    private PanelRepository panelRepository;

    @MockBean
    private SeriesRepository seriesRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ReadingHistoryRepository historyRepository;

    @Test
    @WithMockUser(username = "reader@example.com", roles = "READER")
    void readEpisode_shouldReturnReadSeriesView_andSaveHistory() throws Exception {
        // Mock episode
        Episode episode = new Episode();
        episode.setId(1L);
        episode.setTitle("Episode 1");
        episode.setEpisodeText("Some text");

        Series series = new Series();
        series.setId(10L);
        episode.setSeries(series);

        // Mock panels
        List<Panel> panels = List.of(new Panel());
        List<Episode> episodes = List.of(episode);

        when(episodeRepository.findById(1L)).thenReturn(Optional.of(episode));
        when(panelRepository.findByEpisodeOrderByPanelOrderAsc(episode)).thenReturn(panels);
        when(episodeRepository.findBySeriesOrderByEpisodeNumberAsc(series)).thenReturn(episodes);

        // Mock user
        User user = new User();
        user.setId(100L);
        user.setEmail("reader@example.com");
        when(userRepository.findByEmail("reader@example.com")).thenReturn(Optional.of(user));

        // Mock reading history
        when(historyRepository.findByUserIdAndSeriesId(100L, 10L)).thenReturn(Optional.empty());
        when(historyRepository.save(any(ReadingHistory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(get("/reader/readEpisode/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("read_series"))
                .andExpect(model().attributeExists("series"))
                .andExpect(model().attributeExists("episodes"))
                .andExpect(model().attributeExists("panels"))
                .andExpect(model().attribute("episodeTitle", "Episode 1"))
                .andExpect(model().attribute("episodeText", "Some text"));
    }

    @Test
    @WithMockUser(roles = "CREATOR")
    void backPage_asCreator_shouldRedirectToAddEpisode() throws Exception {
        mockMvc.perform(get("/reader/back")
                        .param("seriesId", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/series/addEpisode/10"));
    }

    @Test
    @WithMockUser(roles = "READER")
    void backPage_asReader_shouldRedirectToReaderHome() throws Exception {
        mockMvc.perform(get("/reader/back")
                        .param("seriesId", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reader_home"));
    }
}