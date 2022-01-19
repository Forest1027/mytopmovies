package com.forest.mytopmovies.service.movie;

import com.forest.mytopmovies.entity.Genre;

import java.util.Optional;

public interface GenreService {

    Optional<Genre> findGenreByTMDBId(int id);

}
