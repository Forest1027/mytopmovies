package com.forest.mytopmovies.service.movie.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.forest.mytopmovies.datamodels.entity.Movie;
import com.forest.mytopmovies.datamodels.entity.MovieList;
import com.forest.mytopmovies.datamodels.entity.MovieMovieList;
import com.forest.mytopmovies.datamodels.entity.User;
import com.forest.mytopmovies.datamodels.params.movie.MovieListMovieUpdateParam;
import com.forest.mytopmovies.datamodels.params.movie.MovieListParam;
import com.forest.mytopmovies.datamodels.params.movie.MovieListUpdateParam;
import com.forest.mytopmovies.datamodels.pojos.MovieListPojo;
import com.forest.mytopmovies.datamodels.pojos.PagePojo;
import com.forest.mytopmovies.exceptions.MovieListNotFoundException;
import com.forest.mytopmovies.exceptions.TMDBHttpRequestException;
import com.forest.mytopmovies.repository.movie.MovieListRepository;
import com.forest.mytopmovies.repository.movie.MovieMovieListRepository;
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

    private final MovieListRepository movieListRepository;

    private final MovieMovieListRepository movieMovieListRepository;

    private ExternalMovieDBApiUtil externalMovieDBApiUtil;

    @Override
    public MovieListPojo createMovieList(MovieListParam movieListParam, User user) throws JsonProcessingException {
        MovieList movieList = PojoEntityParamDtoConverter.convertMovieListParamToEntity(movieListParam);
        movieList.setUser(user);
        movieList = movieListRepository.saveAndFlush(movieList);
        saveEligibleMoviesToList(movieListParam.movies(), movieList);
        return getMovieListsByUserAndId(movieList.getId(), user);
    }

    @Override
    public String deleteMovieList(int id, User user) {
        Optional<MovieList> movieList = movieListRepository.findById(id);
        if (!movieList.isPresent()) {
            throw new MovieListNotFoundException(id);
        }
        movieMovieListRepository.deleteAllByMovieListId(id);
        movieListRepository.deleteById(id);
        return "Successfully deleted movie list with id " + id;
    }

    @Override
    public MovieListPojo updateMovieList(MovieListUpdateParam movieListParam, User user) {
        Optional<MovieList> movieListOpt = movieListRepository.findById(movieListParam.id());
        if (!movieListOpt.isPresent()) {
            throw new MovieListNotFoundException(movieListParam.id());
        }
        MovieList movieList = movieListOpt.get();
        movieList.setMovieListName(movieListParam.movieListName());
        movieList.setDescription(movieListParam.description());
        movieListRepository.saveAndFlush(movieList);

        // get content of related movies
        Set<Movie> eligibleMoviesForList = getEligibleMoviesForList(movieMovieListRepository.findAllByMovieListId(movieListParam.id()).stream().map(MovieMovieList::getMovieId).toList());
        return PojoEntityParamDtoConverter.convertMovieListEntityToPojo(movieList, eligibleMoviesForList.stream().toList());
    }

    @Override
    public PagePojo<MovieListPojo> getMovieLists(String name, Integer page, User user) {
        if (page == null) page = 1;
        Pageable pageable = Pageable.ofSize(5).withPage(page - 1);
        Page<MovieList> queryRes;
        if (name == null) {
            queryRes = movieListRepository.findAllByUserId(user.getId(), pageable);
        } else {
            queryRes = movieListRepository.findAllByUserIdAndMovieListName(user.getId(), name, pageable);
        }

        return PagePojo.<MovieListPojo>builder()
                .totalPages(queryRes.getTotalPages())
                .totalResults(queryRes.getNumberOfElements())
                .page(page)
                .results(queryRes.getContent().stream()
                        .map(movieList -> {
                            List<Integer> movieIds = movieMovieListRepository.findAllByMovieListId(movieList.getId()).stream().map(MovieMovieList::getMovieId).toList();
                            return PojoEntityParamDtoConverter.convertMovieListEntityToPojo(movieList, getEligibleMoviesForList(movieIds).stream().toList());
                        })
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
        saveEligibleMoviesToList(movieListParam.movies(), movieList);
        return getMovieListsByUserAndId(movieListParam.id(), user);
    }

    @Override
    public MovieListPojo getMovieListsByUserAndId(int id, User user) {
        Optional<MovieList> movieListOpt = movieListRepository.findByUserIdAndId(user.getId(), id);
        if (!movieListOpt.isPresent()) {
            throw new MovieListNotFoundException(id);
        }
        MovieList movieList = movieListOpt.get();
        List<Integer> movieIds = movieMovieListRepository.findAllByMovieListId(movieList.getId()).stream().map(MovieMovieList::getMovieId).toList();
        return PojoEntityParamDtoConverter.convertMovieListEntityToPojo(movieList, getEligibleMoviesForList(movieIds).stream().toList());

    }

    @Override
    public MovieListPojo deleteFromMovieList(MovieListMovieUpdateParam movieListParam, User user) {
        Optional<MovieList> movieListOpt = movieListRepository.findById(movieListParam.id());
        if (!movieListOpt.isPresent()) {
            throw new MovieListNotFoundException(movieListParam.id());
        }
        MovieList movieList = movieListOpt.get();
        movieListParam.movies().forEach(movieId -> movieMovieListRepository.deleteByMovieListIdAndMovieId(movieList.getId(), movieId));
        return getMovieListsByUserAndId(movieListParam.id(), user);
    }

    private Set<MovieMovieList> getEligibleMovieMovieListsForList(Set<Movie> movies, MovieList movieList) {
        return movies.stream()
                .map(movie -> MovieMovieList.builder().movieId(movie.getId()).movieList(movieList).build())
                .collect(Collectors.toSet());
    }

    private Set<Movie> getEligibleMoviesForList(List<Integer> movieIds) {
        return movieIds.stream()
                .map(this::retrieveMovieById)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }


    private Movie retrieveMovieById(int id) {
        try {
            return externalMovieDBApiUtil.searchMovieById(id);
        } catch (JsonProcessingException | TMDBHttpRequestException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    private Set<Movie> saveEligibleMoviesToList(List<Integer> movieIds, MovieList movieList) {
        Set<Movie> eligibleMovies = getEligibleMoviesForList(movieIds);
        Set<MovieMovieList> eligibleMovieMovieLists = getEligibleMovieMovieListsForList(eligibleMovies, movieList);
        movieMovieListRepository.saveAllAndFlush(eligibleMovieMovieLists);
        return eligibleMovies;
    }

}
