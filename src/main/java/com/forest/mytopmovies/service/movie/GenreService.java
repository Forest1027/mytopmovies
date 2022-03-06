package com.forest.mytopmovies.service.movie;

import com.forest.mytopmovies.datamodels.entity.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    Optional<Genre> findGenreByTMDBId(int id);

    List<Genre> saveAllGenres(List<Genre> genres);

}
