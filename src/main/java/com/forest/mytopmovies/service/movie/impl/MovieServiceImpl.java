package com.forest.mytopmovies.service.movie.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.forest.mytopmovies.entity.Movie;
import com.forest.mytopmovies.exceptions.TMDBHttpRequestException;
import com.forest.mytopmovies.pojos.PagePojo;
import com.forest.mytopmovies.service.movie.MovieService;
import com.forest.mytopmovies.utils.ExternalMovieDBApiUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {
    private ExternalMovieDBApiUtil externalMovieDBApiUtil;

    @Override
    public PagePojo<Movie> searchTMDBMovieByName(String movieName, int page) throws TMDBHttpRequestException, JsonProcessingException {
        return externalMovieDBApiUtil.searchMovies(movieName, page);
    }

}
