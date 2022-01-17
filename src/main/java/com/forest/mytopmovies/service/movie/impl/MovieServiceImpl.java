package com.forest.mytopmovies.service.movie.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.forest.mytopmovies.exceptions.TMDBHttpRequestException;
import com.forest.mytopmovies.pojos.Page;
import com.forest.mytopmovies.service.movie.MovieService;
import com.forest.mytopmovies.utils.ExternalMovieDBApiUtil;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements MovieService {
    private ExternalMovieDBApiUtil externalMovieDBApiUtil;

    public MovieServiceImpl(ExternalMovieDBApiUtil externalMovieDBApiUtil) {
        this.externalMovieDBApiUtil = externalMovieDBApiUtil;
    }

    @Override
    public Page<com.forest.mytopmovies.entity.Movie> searchTMDBMovieByName(String movieName, int page) throws TMDBHttpRequestException, JsonProcessingException {
        return externalMovieDBApiUtil.searchMovies(movieName, page);
    }

}
