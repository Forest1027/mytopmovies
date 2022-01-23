package com.forest.mytopmovies.service.movie;

import com.forest.mytopmovies.entity.MovieList;
import com.forest.mytopmovies.entity.User;
import com.forest.mytopmovies.params.movie.MovieListParam;
import com.forest.mytopmovies.params.movie.MovieListUpdateParam;
import com.forest.mytopmovies.pojos.MovieListPojo;
import com.forest.mytopmovies.pojos.Page;

public interface MovieListService {
    MovieList createMovieList(MovieListParam movieListParam, User user);

    String deleteMovieList(int id, User user);

    MovieList updateMovieList(MovieListUpdateParam movieListParam, User user);

    Page<MovieListPojo> getMovieLists(String name, Integer page, User user);
}
