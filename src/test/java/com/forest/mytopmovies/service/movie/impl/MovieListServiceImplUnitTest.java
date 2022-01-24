package com.forest.mytopmovies.service.movie.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.forest.mytopmovies.entity.Movie;
import com.forest.mytopmovies.entity.MovieList;
import com.forest.mytopmovies.entity.MovieMovieList;
import com.forest.mytopmovies.entity.User;
import com.forest.mytopmovies.params.movie.MovieListParam;
import com.forest.mytopmovies.pojos.MovieListPojo;
import com.forest.mytopmovies.pojos.PagePojo;
import com.forest.mytopmovies.repository.movie.MovieListRepository;
import com.forest.mytopmovies.repository.movie.MovieMovieListRepository;
import com.forest.mytopmovies.service.movie.MovieListService;
import com.forest.mytopmovies.utils.ExternalMovieDBApiUtil;
import com.forest.utils.UnitTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MovieListServiceImplUnitTest extends UnitTest {
    private MovieListService underTest;

    @Mock
    private MovieListRepository movieListRepository;

    @Mock
    private MovieMovieListRepository movieMovieListRepository;

    @Mock
    private ExternalMovieDBApiUtil externalMovieDBApiUtil;

    @BeforeEach
    void setup() {
        underTest = new MovieListServiceImpl(movieListRepository, movieMovieListRepository, externalMovieDBApiUtil);
    }

    @AfterEach
    void teardown() {
        verifyNoMoreInteractions(movieListRepository, movieMovieListRepository, externalMovieDBApiUtil);
    }

    @Test
    void canCreateMovieList() throws JsonProcessingException {
        // given
        String movieListName = "list1";
        String description = "test-description";
        String movieName = "test-movie";
        Integer[] movieIds = {1, 2, 3};

        MovieListParam param = new MovieListParam(movieListName, description, Arrays.asList(movieIds));
        User user = User.builder().username("forest").username("123456").build();
        MovieList mockMovieList = MovieList.builder().movieListName(movieListName).description(description).build();
        Movie mockMovie = Movie.builder().id(1).originalTitle(movieName).build();

        when(movieListRepository.saveAndFlush(any())).thenReturn(mockMovieList);
        when(externalMovieDBApiUtil.searchMovieById(anyInt())).thenReturn(mockMovie);

        // when
        MovieListPojo movieList = underTest.createMovieList(param, user);

        // then
        verify(movieListRepository).saveAndFlush(any());
        verify(movieMovieListRepository).saveAllAndFlush(any());
        verify(externalMovieDBApiUtil, times(movieIds.length)).searchMovieById(anyInt());
        assertThat(movieList.getMovies().get(0).getOriginalTitle()).isEqualTo(movieName);
    }

    @Test
    void deleteMovieList() {
    }

    @Test
    void updateMovieList() {
    }

    @Test
    void canGetMovieListsWithDefaultPage() throws JsonProcessingException {
        // given
        String movieName = "test-movie";
        String movieListName = "list1";
        User user = User.builder().username("forest").username("123456").id("test-id").build();
        Movie mockMovie = Movie.builder().id(1).originalTitle(movieName).build();
        Page<MovieList> mockPage = new PageImpl<>(Arrays.asList(MovieList.builder().movieListName(movieListName).build()));
        List<MovieMovieList> mockMovieMovieLists = Arrays.asList(MovieMovieList.builder().movieList(mockPage.getContent().get(0)).movie(mockMovie).build());

        when(movieListRepository.findAllByUserId(anyString(), any())).thenReturn(mockPage);
        when(externalMovieDBApiUtil.searchMovieById(anyInt())).thenReturn(mockMovie);
        when(movieMovieListRepository.findAllByMovieListId(anyInt())).thenReturn(mockMovieMovieLists);

        // when
        PagePojo<MovieListPojo> movieLists = underTest.getMovieLists(null, null, user);

        // then
        assertThat(movieLists.getPage()).isEqualTo(1);
        verify(movieListRepository).findAllByUserId(anyString(), any());
        verify(externalMovieDBApiUtil).searchMovieById(anyInt());
        verify(movieMovieListRepository).findAllByMovieListId(anyInt());
        assertThat(movieLists.getResults().size()).isEqualTo(1);
        assertThat(movieLists.getResults().get(0).getMovieListName()).isEqualTo(movieListName);
        assertThat(movieLists.getResults().get(0).getMovies().get(0).getOriginalTitle()).isEqualTo(movieName);
    }

    @Test
    void canGetMovieListsWithPassedPage() throws JsonProcessingException {
        // given
        String movieName = "test-movie";
        String movieListName = "list1";
        User user = User.builder().username("forest").username("123456").id("test-id").build();
        Movie mockMovie = Movie.builder().id(1).originalTitle(movieName).build();
        Page<MovieList> mockPage = new PageImpl<>(Arrays.asList(MovieList.builder().movieListName(movieListName).build()));
        List<MovieMovieList> mockMovieMovieLists = Arrays.asList(MovieMovieList.builder().movieList(mockPage.getContent().get(0)).movie(mockMovie).build());

        when(movieListRepository.findAllByUserIdAndMovieListName(anyString(), anyString(), any())).thenReturn(mockPage);
        when(externalMovieDBApiUtil.searchMovieById(anyInt())).thenReturn(mockMovie);
        when(movieMovieListRepository.findAllByMovieListId(anyInt())).thenReturn(mockMovieMovieLists);

        // when
        PagePojo<MovieListPojo> movieLists = underTest.getMovieLists("", 2, user);

        // then
        assertThat(movieLists.getPage()).isEqualTo(2);
        verify(movieListRepository).findAllByUserIdAndMovieListName(anyString(), anyString(), any());
        verify(externalMovieDBApiUtil).searchMovieById(anyInt());
        verify(movieMovieListRepository).findAllByMovieListId(anyInt());
        assertThat(movieLists.getResults().size()).isEqualTo(1);
        assertThat(movieLists.getResults().get(0).getMovieListName()).isEqualTo(movieListName);
        assertThat(movieLists.getResults().get(0).getMovies().get(0).getOriginalTitle()).isEqualTo(movieName);
    }
}