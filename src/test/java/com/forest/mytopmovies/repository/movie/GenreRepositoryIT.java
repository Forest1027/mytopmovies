package com.forest.mytopmovies.repository.movie;

import com.forest.mytopmovies.entity.Genre;
import com.forest.utils.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GenreRepositoryIT extends IntegrationTest {

    @Autowired
    private GenreRepository underTest;

    @BeforeEach
    void setup() {
        Genre genre = Genre.builder().genreName("test-genre").tmdbId(1).build();
        underTest.saveAndFlush(genre);
    }

    @Test
    void canFindOneByTmdbId() {
        // given

        // when
        Optional<Genre> result = underTest.findOneByTmdbId(1);

        // then
        assertThat(result).isPresent();
    }

}