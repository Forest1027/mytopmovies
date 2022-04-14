package com.forest.mytopmovies.service.movie.impl;

import com.forest.mytopmovies.datamodels.entity.Genre;
import com.forest.mytopmovies.repository.movie.GenreRepository;
import com.forest.mytopmovies.service.movie.GenreService;
import com.forest.utils.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

class GenreServiceImplUnitTest extends UnitTest {
    private GenreService underTest;

    @Mock
    private GenreRepository genreRepository;

    @BeforeEach
    void setup() {
        underTest = new GenreServiceImpl(genreRepository);
    }

    @Test
    void canFindGenreByTMDBId() {
        // given
        int id = 1;

        // when
        underTest.findGenreByTMDBId(id);

        // then
        verify(genreRepository).findOneByTmdbId(id);
    }

    @Test
    void canSaveAllGenres() {
        // given
        List<Genre> genres = new ArrayList<>();

        // when
        underTest.saveAllGenres(genres);

        // then
        verify(genreRepository).saveAllAndFlush(genres);
    }

}