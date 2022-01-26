package com.forest.mytopmovies.repository.movie;

import com.forest.mytopmovies.datamodels.entity.MovieList;
import com.forest.mytopmovies.datamodels.entity.MovieMovieList;
import com.forest.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MovieMovieListRepositoryIT extends IntegrationTest {

    @Autowired
    private MovieMovieListRepository underTest;

    @Autowired
    private MovieListRepository movieListRepository;

    @Test
    void canFindAllByMovieListId() {
        // given
        MovieList movieList = MovieList.builder().movieListName("test").build();
        movieList = movieListRepository.saveAndFlush(movieList);
        MovieMovieList movieMovieList = underTest.saveAndFlush(MovieMovieList.builder().movieList(movieList).movieId(2).build());

        // when
        List<MovieMovieList> result = underTest.findAllByMovieListId(movieList.getId());

        // then
        assertThat(result.size()).isEqualTo(1);
    }
}