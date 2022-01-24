package com.forest.mytopmovies.service.movie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.forest.mytopmovies.entity.MovieList;
import com.forest.mytopmovies.entity.User;
import com.forest.mytopmovies.params.movie.MovieListParam;
import com.forest.mytopmovies.params.movie.MovieListUpdateParam;
import com.forest.mytopmovies.pojos.MovieListPojo;
import com.forest.mytopmovies.pojos.PagePojo;

public interface MovieListService {
    MovieListPojo createMovieList(MovieListParam movieListParam, User user) throws JsonProcessingException;

    String deleteMovieList(int id, User user);

    MovieList updateMovieList(MovieListUpdateParam movieListParam, User user);

    PagePojo<MovieListPojo> getMovieLists(String name, Integer page, User user);
}
