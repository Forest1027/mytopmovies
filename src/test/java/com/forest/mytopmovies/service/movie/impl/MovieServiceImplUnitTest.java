package com.forest.mytopmovies.service.movie.impl;

import com.forest.mytopmovies.entity.Movie;
import com.forest.mytopmovies.pojos.PagePojo;
import com.forest.mytopmovies.service.movie.MovieService;
import com.forest.mytopmovies.utils.ExternalMovieDBApiUtil;
import com.forest.utils.UnitTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MovieServiceImplUnitTest extends UnitTest {
    private MovieService underTest;

    @Mock
    private ExternalMovieDBApiUtil externalMovieDBApiUtil;

    @BeforeEach
    void setUp() {
        underTest = new MovieServiceImpl(externalMovieDBApiUtil);
    }

    @Test
    void canSearchMovie() throws Exception {
        // given
        String movieName = "Chicago";
        int page = 1;
        var expectedResult = PagePojo.<Movie>builder().build();

        when(externalMovieDBApiUtil.searchMovies(movieName, page)).thenReturn(expectedResult);

        // when
        PagePojo<Movie> result = underTest.searchTMDBMovieByName(movieName, page);

        // then
        verify(externalMovieDBApiUtil).searchMovies(movieName, page);
        assertThat(result).isEqualTo(expectedResult);
    }

    @AfterEach
    void teardown() {
        verifyNoMoreInteractions(externalMovieDBApiUtil);
    }

}