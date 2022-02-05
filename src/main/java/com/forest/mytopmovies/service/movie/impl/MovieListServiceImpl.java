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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        saveUnSavedMovies(movieListParam.movies());
        Set<Movie> movies = movieListParam.movies().stream()
                .map(movieRepository::findByTmdbId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        // save movie list to db
        MovieList movieList = PojoEntityParamDtoConverter.convertMovieListParamToEntity(movieListParam);
        movieList.setUser(user);
        movieList.setMovies(movies);
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
        Optional<MovieList> optionalMovieList = movieListRepository.findByUserIdAndId(user.getId(), movieListParam.id());
        if (!optionalMovieList.isPresent()) {
            throw new MovieListNotFoundException(movieListParam.id());
        }
        MovieList movieList = optionalMovieList.get();
        saveUnSavedMovies(movieListParam.movies());
        Set<Movie> movies = movieListParam.movies().stream()
                .map(movieRepository::findByTmdbId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        movieList.setMovies(movies);
        movieList.setDescription(movieListParam.description());
        movieList.setMovieListName(movieListParam.movieListName());
        return PojoEntityParamDtoConverter.convertMovieListEntityToPojo(movieList);
    }

    @Override
    public PagePojo<MovieListPojo> getMovieLists(String name, Integer page, User user) {
        if (page == null) page = 1;
        Pageable pageable = Pageable.ofSize(5).withPage(page - 1);
        Page<MovieList> queryRes = movieListRepository.findAllByUserIdAndMovieListName(user.getId(), name, pageable);
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
        Optional<MovieList> movieListOpt = movieListRepository.findById(movieListParam.id());
        if (!movieListOpt.isPresent()) {
            throw new MovieListNotFoundException(movieListParam.id());
        }
        MovieList movieList = movieListOpt.get();
        saveUnSavedMovies(movieListParam.movies());
        Set<Movie> movies = movieListParam.movies().stream()
                .map(id -> movieRepository.findByTmdbId(id).get())
                .collect(Collectors.toSet());
        for (Movie movie : movies) {
            movieList.getMovies().add(movie);
        }
        return getMovieListsByUserAndId(movieListParam.id(), user);
    }

    @Override
    public MovieListPojo getMovieListsByUserAndId(int id, User user) {
        Optional<MovieList> movieListOpt = movieListRepository.findByUserIdAndId(user.getId(), id);
        if (!movieListOpt.isPresent()) {
            throw new MovieListNotFoundException(id);
        }
        MovieList movieList = movieListOpt.get();
        return PojoEntityParamDtoConverter.convertMovieListEntityToPojo(movieList);
    }


    private Movie retrieveMovieById(int id) {
        try {
            return externalMovieDBApiUtil.searchMovieById(id);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    private Set<Integer> findNewMoviesToBeSaved(List<Integer> movieIds) {
        return movieIds.stream()
                .filter(id -> !movieRepository.findByTmdbId(id).isPresent())
                .collect(Collectors.toSet());
    }

    private void saveUnSavedMovies(List<Integer> movieIds) {
        Set<Integer> moviesToBeSaved = findNewMoviesToBeSaved(movieIds);
        if (!moviesToBeSaved.isEmpty()) {
            Set<Movie> movieRestrieved = moviesToBeSaved.stream()
                    .map(this::retrieveMovieById)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            // save movies to db
            movieRepository.saveAllAndFlush(movieRestrieved);
        }
    }

}
