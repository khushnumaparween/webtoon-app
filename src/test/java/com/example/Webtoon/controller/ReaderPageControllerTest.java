package com.example.Webtoon.controller;

import com.example.Webtoon.model.Series;
import com.example.Webtoon.service.SeriesService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(ReaderPageController.class)
public class ReaderPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeriesService seriesService; // Mock the service instead of repository

    @Test
    public void testSearchSeries() throws Exception {
        // Mock some series data
        Series series = new Series();
        series.setId(1L);
        series.setTitle("Test Series");

        Mockito.when(seriesService.search("Test")).thenReturn(List.of(series));

        mockMvc.perform(get("/reader/search")
                        .param("query", "Test"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("seriesList"));
    }
}