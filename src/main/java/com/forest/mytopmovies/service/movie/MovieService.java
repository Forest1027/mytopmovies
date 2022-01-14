package com.forest.mytopmovies.service.movie;


import com.forest.mytopmovies.entity.Movie;
import com.forest.mytopmovies.pojos.Page;

public interface MovieService {

    Page<Movie> searchTMDBMovieByName(String movieName, int page) throws Exception;

}
