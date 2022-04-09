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
import com.forest.mytopmovies.exceptions.MovieListNotFoundException;
import com.forest.mytopmovies.repository.movie.MovieListRepository;
import com.forest.mytopmovies.repository.movie.MovieRepository;
import com.forest.mytopmovies.service.movie.MovieListService;
import com.forest.mytopmovies.utils.ExternalMovieDBApiUtil;
import com.forest.mytopmovies.utils.PojoEntityParamDtoConverter;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class MovieListServiceImpl implements MovieListService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieListServiceImpl.class);

    private final MovieRepository movieRepository;

    private final MovieListRepository movieListRepository;

    private ExternalMovieDBApiUtil externalMovieDBApiUtil;

    @Override
    public MovieListPojo createMovieList(MovieListParam movieListParam, User user) {
        Set<Movie> savedMovies = saveUnSavedMovies(movieListParam.movies());
        // save movie list to db
        MovieList movieList = PojoEntityParamDtoConverter.convertMovieListParamToEntity(movieListParam);
        movieList.setUser(user);
        movieList.setMovies(savedMovies);
        movieListRepository.saveAndFlush(movieList);
        return PojoEntityParamDtoConverter.convertMovieListEntityToPojo(movieList);
    }

    @Override
    public String deleteMovieList(int id, User user) {
        movieListRepository.findById(id).ifPresentOrElse(movieList -> movieListRepository.deleteById(id), () -> {
            throw new MovieListNotFoundException(id);
        });
        return "Successfully deleted movie list with id " + id;
    }

    @Override
    public MovieListPojo updateMovieList(MovieListUpdateParam movieListParam, User user) {
        // retrieve movie list
        return movieListRepository.findByUserIdAndId(user.getId(), movieListParam.id())
                .map(movieList -> {
                    movieList.setMovies(saveUnSavedMovies(movieListParam.movies()));
                    movieList.setDescription(movieListParam.description());
                    movieList.setMovieListName(movieListParam.movieListName());
                    return PojoEntityParamDtoConverter.convertMovieListEntityToPojo(movieList);
                }).orElseThrow(() -> new MovieListNotFoundException(movieListParam.id()));
    }

    @Override
    public PagePojo<MovieListPojo> getMovieLists(String name, Integer page, User user) {
        if (page == null) page = 1;
        Pageable pageable = Pageable.ofSize(5).withPage(page - 1);
        Page<MovieList> queryRes = movieListRepository.findAllByUserIdAndMovieListNameIgnoreCase(user.getId(), name, pageable);
        return PagePojo.<MovieListPojo>builder()
                .totalPages(queryRes.getTotalPages())
                .totalResults(queryRes.getNumberOfElements())
                .page(page)
                .results(queryRes.getContent().stream()
                        .map(PojoEntityParamDtoConverter::convertMovieListEntityToPojo)
                        .toList())
                .build();
    }

    @Override
    public MovieListPojo addMoviesToList(MovieListMovieUpdateParam movieListParam, User user) {
        return movieListRepository.findById(movieListParam.id())
                .map(movieList -> {
                    for (Movie movie : saveUnSavedMovies(movieListParam.movies())) {
                        movieList.getMovies().add(movie);
                    }
                    return getMovieListsByUserAndId(movieListParam.id(), user);
                }).orElseThrow(() -> new MovieListNotFoundException(movieListParam.id()));
    }

    @Override
    public MovieListPojo getMovieListsByUserAndId(int id, User user) {
        return movieListRepository.findByUserIdAndId(user.getId(), id)
                .map(PojoEntityParamDtoConverter::convertMovieListEntityToPojo)
                .orElseThrow(() -> new MovieListNotFoundException(id));
    }

    @Override
    public PagePojo<MovieListPojo> getAllMovieListsByUser(User user, Integer page) {
        if (page == null) page = 1;
        Pageable pageable = Pageable.ofSize(5).withPage(page - 1);
        Page<MovieList> queryRes = movieListRepository.findAllByUserId(user.getId(), pageable);
        return PagePojo.<MovieListPojo>builder()
                .totalPages(queryRes.getTotalPages())
                .totalResults(queryRes.getNumberOfElements())
                .page(page)
                .results(queryRes.getContent().stream()
                        .map(PojoEntityParamDtoConverter::convertMovieListEntityToPojo)
                        .toList())
                .build();
    }


    private Movie retrieveMovieById(int id) {
        try {
            return externalMovieDBApiUtil.searchMovieById(id);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    private Pair<Set<Movie>, Set<Integer>> findNewMoviesToBeSaved(List<Integer> movieIds) {
        Set<Movie> moviesSaved = new HashSet<>();
        Set<Integer> movieTeBeSaved = new HashSet<>();
        for (int id : movieIds) {
            Optional<Movie> movieOptional = movieRepository.findByTmdbId(id);
            if (movieOptional.isPresent()) moviesSaved.add(movieOptional.get());
            else movieTeBeSaved.add(id);
        }
        return new MutablePair<>(moviesSaved, movieTeBeSaved);
    }

    private Set<Movie> saveUnSavedMovies(List<Integer> movieIds) {
        Pair<Set<Movie>, Set<Integer>> moviesPair = findNewMoviesToBeSaved(movieIds);
        Set<Movie> moviesSaved = moviesPair.getLeft();
        Set<Integer> moviesToBeSaved = moviesPair.getRight();
        if (!moviesToBeSaved.isEmpty()) {
            Set<Movie> movieRetrieved = moviesToBeSaved.stream()
                    .map(this::retrieveMovieById)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            // save movies to db
            List<Movie> movies = movieRepository.saveAllAndFlush(movieRetrieved);
            moviesSaved.addAll(movies);
        }
        return moviesSaved;
    }

}
