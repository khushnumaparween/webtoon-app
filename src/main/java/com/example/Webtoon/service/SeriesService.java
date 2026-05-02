package com.example.Webtoon.service;

import com.example.Webtoon.model.Series;
import com.example.Webtoon.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeriesService {

    @Autowired
    private SeriesRepository seriesRepository;

    public List<Series> search(String query) {
        return seriesRepository.searchComics(query);
    }
}