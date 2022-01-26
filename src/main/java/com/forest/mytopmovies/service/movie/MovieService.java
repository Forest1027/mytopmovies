package com.forest.mytopmovies.service.movie;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.forest.mytopmovies.datamodels.pojos.MoviePojo;
import com.forest.mytopmovies.datamodels.pojos.PagePojo;
import com.forest.mytopmovies.exceptions.TMDBHttpRequestException;

public interface MovieService {

    PagePojo<MoviePojo> searchTMDBMovieByName(String movieName, int page) throws TMDBHttpRequestException, JsonProcessingException;

}
