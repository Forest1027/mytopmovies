package com.forest.mytopmovies.service.movie;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.forest.mytopmovies.entity.Movie;
import com.forest.mytopmovies.exceptions.TMDBHttpRequestException;
import com.forest.mytopmovies.pojos.PagePojo;

public interface MovieService {

    PagePojo<Movie> searchTMDBMovieByName(String movieName, int page) throws TMDBHttpRequestException, JsonProcessingException;

}
