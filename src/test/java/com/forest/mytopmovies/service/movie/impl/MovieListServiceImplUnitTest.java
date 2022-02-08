package com.forest.mytopmovies.service.movie.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.forest.mytopmovies.datamodels.entity.Movie;
import com.forest.mytopmovies.datamodels.entity.MovieList;
import com.forest.mytopmovies.datamodels.entity.User;
import com.forest.mytopmovies.datamodels.params.movie.MovieListMovieUpdateParam;
import com.forest.mytopmovies.datamodels.params.movie.MovieListParam;
import com.forest.mytopmovies.datamodels.params.movie.MovieListUpdateParam;
import com.forest.mytopmovies.datamodels.pojos.MovieListPojo;
import com.forest.mytopmovies.datamodels.pojos.PagePojo;
import com.forest.mytopmovies.repository.movie.MovieListRepository;
import com.forest.mytopmovies.repository.movie.MovieRepository;
import com.forest.mytopmovies.service.movie.MovieListService;
import com.forest.mytopmovies.utils.ExternalMovieDBApiUtil;
import com.forest.utils.UnitTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class MovieListServiceImplUnitTest extends UnitTest {
    private static String movieListName = "list1";

    private static String description = "test-description";

    private static String movieName = "test-movie";

    private static Integer[] movieIds = {1, 2, 3};

    private MovieListService underTest;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieListRepository movieListRepository;

    @Mock
    private ExternalMovieDBApiUtil externalMovieDBApiUtil;

    @BeforeEach
    void setup() {
        underTest = new MovieListServiceImpl(movieRepository, movieListRepository, externalMovieDBApiUtil);
    }

    @AfterEach
    void teardown() {
        verifyNoMoreInteractions(movieListRepository, movieRepository, externalMovieDBApiUtil);
    }

    @Test
    void canCreateMovieList() throws JsonProcessingException {
        // given
        MovieListParam param = new MovieListParam(movieListName, description, Arrays.asList(movieIds));
        User user = User.builder().username("forest").username("123456").build();
        MovieList mockMovieList = MovieList.builder().movieListName(movieListName).description(description).build();
        Movie mockMovie = Movie.builder().id(1).originalTitle(movieName).build();

        when(movieRepository.findByTmdbId(anyInt())).thenReturn(Optional.of(mockMovie));
        when(movieListRepository.saveAndFlush(any())).thenReturn(mockMovieList);

        // when
        MovieListPojo movieList = underTest.createMovieList(param, user);

        // then
        verify(movieRepository, times(movieIds.length)).findByTmdbId(anyInt());
        verify(movieListRepository).saveAndFlush(any());
        assertThat(movieList.getMovies().get(0).getOriginalTitle()).isEqualTo(movieName);
    }

    @Test
    void canCreateMovieListWithMovieToSave() throws JsonProcessingException {
        // given
        MovieListParam param = new MovieListParam(movieListName, description, Arrays.asList(movieIds));
        User user = User.builder().username("forest").username("123456").build();
        MovieList mockMovieList = MovieList.builder().movieListName(movieListName).description(description).build();
        Movie mockMovie = Movie.builder().id(1).originalTitle(movieName).build();

        when(movieRepository.findByTmdbId(anyInt())).thenReturn(Optional.empty());
        when(movieListRepository.saveAndFlush(any())).thenReturn(mockMovieList);
        when(externalMovieDBApiUtil.searchMovieById(anyInt())).thenReturn(mockMovie);

        // when
        MovieListPojo movieList = underTest.createMovieList(param, user);

        // then
        verify(movieRepository, times(movieIds.length)).findByTmdbId(anyInt());
        verify(movieRepository).saveAllAndFlush(any());
        verify(movieListRepository).saveAndFlush(any());
        assertThat(movieList.getMovieListName()).isEqualTo(movieListName);
    }

    @Test
    void canDeleteMovieList() {
        // given
        int movieListId = 1;
        MovieList mockMovieList = MovieList.builder().id(movieListId).movieListName(movieListName).description(description).build();
        when(movieListRepository.findById(anyInt())).thenReturn(Optional.of(mockMovieList));

        // when
        underTest.deleteMovieList(movieListId, any());

        // then
        verify(movieListRepository).findById(movieListId);
        verify(movieListRepository).deleteById(movieListId);
    }

    @Test
    void canUpdateMovieList() {
        // given
        MovieListUpdateParam param = new MovieListUpdateParam(1, movieListName, description, Arrays.asList(movieIds));
        User user = User.builder().username("forest").username("123456").id("test").build();
        MovieList mockMovieList = MovieList.builder().id(1).movieListName(movieListName).description(description).build();
        Movie mockMovie = Movie.builder().id(1).originalTitle(movieName).build();

        when(movieListRepository.findByUserIdAndId(user.getId(), mockMovieList.getId())).thenReturn(Optional.of(mockMovieList));
        when(movieRepository.findByTmdbId(anyInt())).thenReturn(Optional.of(mockMovie));

        // when
        MovieListPojo result = underTest.updateMovieList(param, user);

        // then
        verify(movieListRepository).findByUserIdAndId(anyString(), anyInt());
        verify(movieRepository, times(movieIds.length)).findByTmdbId(anyInt());
        assertThat(result.getMovieListName()).isEqualTo(movieListName);
        assertThat(result.getDescription()).isEqualTo(description);
    }

    @Test
    void canGetMovieLists() {
        // given
        MovieList mockMovieList = MovieList.builder().movieListName(movieListName).description(description).build();
        Pageable pageable = Pageable.ofSize(5).withPage(0);
        Page<MovieList> mockPage = new PageImpl<>(Collections.singletonList(mockMovieList), pageable, 0);
        User user = User.builder().username("forest").username("123456").id("test").build();
        when(movieListRepository.findAllByUserIdAndMovieListName(user.getId(), mockMovieList.getMovieListName(), pageable)).thenReturn(mockPage);

        // when
        PagePojo<MovieListPojo> result = underTest.getMovieLists(mockMovieList.getMovieListName(), null, user);

        // then
        verify(movieListRepository).findAllByUserIdAndMovieListName(user.getId(), mockMovieList.getMovieListName(), pageable);
        assertThat(result.getResults().get(0).getMovieListName()).isEqualTo(movieListName);
    }

    @Test
    void canAddMovieToList() {
        // given
        MovieListMovieUpdateParam param = new MovieListMovieUpdateParam(1, Arrays.asList(movieIds));
        User user = User.builder().username("forest").username("123456").build();
        MovieList mockMovieList = MovieList.builder().movieListName(movieListName).description(description).id(1).movies(new HashSet<>()).build();
        Movie mockMovie = Movie.builder().id(1).originalTitle(movieName).build();

        when(movieRepository.findByTmdbId(anyInt())).thenReturn(Optional.of(mockMovie));
        when(movieListRepository.findById(param.id())).thenReturn(Optional.of(mockMovieList));
        when(movieListRepository.findByUserIdAndId(user.getId(), mockMovieList.getId())).thenReturn(Optional.of(mockMovieList));

        // when
        MovieListPojo result = underTest.addMoviesToList(param, user);

        // then
        verify(movieRepository, times(movieIds.length)).findByTmdbId(anyInt());
        verify(movieListRepository).findById(mockMovieList.getId());
        verify(movieListRepository).findByUserIdAndId(user.getId(), mockMovieList.getId());
    }

    @Test
    void canGetMovieListsByUserAndId() {
        // given
        MovieListMovieUpdateParam param = new MovieListMovieUpdateParam(1, Arrays.asList(movieIds));
        User user = User.builder().username("forest").username("123456").build();
        MovieList mockMovieList = MovieList.builder().movieListName(movieListName).description(description).id(1).movies(new HashSet<>()).build();
        Movie mockMovie = Movie.builder().id(1).originalTitle(movieName).build();
        when(movieListRepository.findByUserIdAndId(user.getId(), mockMovieList.getId())).thenReturn(Optional.of(mockMovieList));

        // when
        MovieListPojo result = underTest.getMovieListsByUserAndId(mockMovieList.getId(), user);

        // then
        verify(movieListRepository).findByUserIdAndId(user.getId(), mockMovieList.getId());
        assertThat(result.getMovieListName()).isEqualTo(movieListName);
    }
}
