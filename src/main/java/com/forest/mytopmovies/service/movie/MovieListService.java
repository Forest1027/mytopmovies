package com.forest.mytopmovies.service.movie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.forest.mytopmovies.datamodels.entity.User;
import com.forest.mytopmovies.datamodels.params.movie.MovieListMovieUpdateParam;
import com.forest.mytopmovies.datamodels.params.movie.MovieListParam;
import com.forest.mytopmovies.datamodels.params.movie.MovieListUpdateParam;
import com.forest.mytopmovies.datamodels.pojos.MovieListPojo;
import com.forest.mytopmovies.datamodels.pojos.PagePojo;

public interface MovieListService {
    MovieListPojo createMovieList(MovieListParam movieListParam, User user) throws JsonProcessingException;

    String deleteMovieList(int id, User user);

    MovieListPojo updateMovieList(MovieListUpdateParam movieListParam, User user);

    PagePojo<MovieListPojo> getMovieLists(String name, Integer page, User user);

    MovieListPojo addMoviesToList(MovieListMovieUpdateParam movieListParam, User user);

    MovieListPojo getMovieListsByUserAndId(int id, User user);

    MovieListPojo deleteFromMovieList(MovieListMovieUpdateParam movieListParam, User user);
}
