package com.forest.mytopmovies.service.movie.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forest.mytopmovies.exceptions.MovieNotFoundException;
import com.forest.mytopmovies.pojos.Movie;
import com.forest.mytopmovies.pojos.Page;
import com.forest.mytopmovies.service.movie.MovieService;
import com.forest.mytopmovies.utils.MoviePojoEntityConverter;
import com.forest.mytopmovies.utils.TMDBApiUtil;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {
    private TMDBApiUtil tmdbApiUtil;

    public MovieServiceImpl(TMDBApiUtil tmdbApiUtil) {
        this.tmdbApiUtil = tmdbApiUtil;
    }

    @Override
    public Page<com.forest.mytopmovies.entity.Movie> searchTMDBMovieByName(String movieName, int page) throws Exception {
        HttpResponse<String> response = tmdbApiUtil.searchMovies(movieName, page);
        ObjectMapper objectMapper = new ObjectMapper();
        if (response.statusCode() != 200) {
            JsonNode jsonNode = objectMapper.readTree(response.body());
            throw new MovieNotFoundException(jsonNode.get("status_message").asText());
        }
        Page<Movie> queryResult = objectMapper.readValue(response.body(), new TypeReference<>() {
        });
        List<com.forest.mytopmovies.entity.Movie> convertedMovies = transferMoviePojoListToEntityList(queryResult.getResults());
        return Page.<com.forest.mytopmovies.entity.Movie>builder().page(queryResult.getPage())
                .total_results(queryResult.getTotal_results())
                .total_pages(queryResult.getTotal_pages()).results(convertedMovies).build();
    }

    private List<com.forest.mytopmovies.entity.Movie> transferMoviePojoListToEntityList(List<Movie> movies) {
        return movies.stream()
                .map(movie -> MoviePojoEntityConverter.convertMoviePojoToEntity(movie))
                .toList();
    }
}
