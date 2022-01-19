package com.forest.mytopmovies.service.movie.impl;

import com.forest.mytopmovies.entity.Genre;
import com.forest.mytopmovies.repository.movie.GenreRepository;
import com.forest.mytopmovies.service.movie.GenreService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {
    private GenreRepository repository;

    public GenreServiceImpl(GenreRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Genre> findGenreByTMDBId(int id) {
        return repository.findOneByTmdbId(id);
    }
}
