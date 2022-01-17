package com.forest.mytopmovies.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forest.mytopmovies.constants.TMDBConstants;
import com.forest.mytopmovies.exceptions.TMDBHttpRequestException;
import com.forest.mytopmovies.pojos.Movie;
import com.forest.mytopmovies.pojos.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import java.util.List;

@Component
public class ExternalMovieDBApiUtil {

    private final TMDBConstants tmdbConstants;

    public ExternalMovieDBApiUtil(TMDBConstants tmdbConstants) {
        this.tmdbConstants = tmdbConstants;
    }

    public Page<com.forest.mytopmovies.entity.Movie> searchMovies(String movieName, int page) throws TMDBHttpRequestException, JsonProcessingException {
        String uri = tmdbConstants.searchMovieAPI + "?api_key=" + tmdbConstants.tmdbKey + "&page=" + page + "&query=" + UriUtils.encode(movieName, "UTF-8");
        String response =  HttpUtil.get(tmdbConstants.baseUrl, uri);
        ObjectMapper objectMapper = new ObjectMapper();
        Page<Movie> queryResult = objectMapper.readValue(response, new TypeReference<>() {});
        List<com.forest.mytopmovies.entity.Movie> convertedMovies = MoviePojoEntityConverter.transferMoviePojoListToEntityList(queryResult.getResults());

        return Page.<com.forest.mytopmovies.entity.Movie>builder().page(queryResult.getPage())
                .total_results(queryResult.getTotal_results())
                .total_pages(queryResult.getTotal_pages()).results(convertedMovies).build();
    }

}
