package com.forest.mytopmovies.service.movie;

import com.forest.mytopmovies.entity.MovieList;
import com.forest.mytopmovies.params.movie.MovieListParam;

public interface MovieListService {
    MovieList createMovieList(MovieListParam movieListParam);
}
