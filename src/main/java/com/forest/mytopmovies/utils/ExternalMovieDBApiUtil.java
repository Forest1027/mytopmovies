package com.forest.mytopmovies.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forest.mytopmovies.constants.TMDBConstants;
import com.forest.mytopmovies.datamodels.dtos.MovieDto;
import com.forest.mytopmovies.datamodels.dtos.PageDto;
import com.forest.mytopmovies.datamodels.entity.Movie;
import com.forest.mytopmovies.exceptions.TMDBHttpRequestException;
import com.forest.mytopmovies.service.movie.GenreService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import java.util.List;

@Component
@AllArgsConstructor
public class ExternalMovieDBApiUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalMovieDBApiUtil.class);

    private final TMDBConstants tmdbConstants;

    private final GenreService genreService;

    public PageDto<Movie> searchMovies(String movieName, int page) throws TMDBHttpRequestException, JsonProcessingException {
        String uri = tmdbConstants.searchMovieAPI + "?api_key=" + tmdbConstants.tmdbKey + "&page=" + page + "&query=" + UriUtils.encode(movieName, "UTF-8");
        String response = HttpUtil.get(tmdbConstants.baseUrl, uri);
        ObjectMapper objectMapper = new ObjectMapper();
        PageDto<MovieDto> queryResult = objectMapper.readValue(response, new TypeReference<>() {
        });
        List<Movie> convertedMovies = PojoEntityParamDtoConverter.convertMoviePojoListToEntityList(queryResult.getResults(), genreService);

        return PageDto.<Movie>builder().page(queryResult.getPage())
                .total_results(queryResult.getTotal_results())
                .total_pages(queryResult.getTotal_pages()).results(convertedMovies).build();
    }

    public Movie searchMovieById(int movieId) throws TMDBHttpRequestException, JsonProcessingException {
        String uri = tmdbConstants.searchMovieByIdAPI + "/" + movieId + "?api_key=" + tmdbConstants.tmdbKey;
        String response = HttpUtil.get(tmdbConstants.baseUrl, uri);
        ObjectMapper objectMapper = new ObjectMapper();
        MovieDto queryResult = objectMapper.readValue(response, new TypeReference<>() {
        });
        return PojoEntityParamDtoConverter.convertMovieDtoToEntity(queryResult, genreService);
    }

}
