package com.forest.mytopmovies.service.movie.impl;

import com.forest.mytopmovies.datamodels.entity.Genre;
import com.forest.mytopmovies.repository.movie.GenreRepository;
import com.forest.mytopmovies.service.movie.GenreService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {
    private GenreRepository repository;

    @Override
    public Optional<Genre> findGenreByTMDBId(int id) {
        return repository.findOneByTmdbId(id);
    }
}
