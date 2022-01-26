package com.forest.mytopmovies.service.movie.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.forest.mytopmovies.datamodels.entity.Movie;
import com.forest.mytopmovies.datamodels.entity.MovieList;
import com.forest.mytopmovies.datamodels.entity.MovieMovieList;
import com.forest.mytopmovies.datamodels.entity.User;
import com.forest.mytopmovies.datamodels.params.movie.MovieListParam;
import com.forest.mytopmovies.datamodels.params.movie.MovieListUpdateParam;
import com.forest.mytopmovies.datamodels.pojos.MovieListPojo;
import com.forest.mytopmovies.datamodels.pojos.PagePojo;
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

        Set<Movie> eligibleMovies = getEligibleMoviesForList(movieListParam.movies());

        Set<MovieMovieList> eligibleMovieMovieLists = getEligibleMovieMovieListsForList(eligibleMovies, movieList);
        movieMovieListRepository.saveAllAndFlush(eligibleMovieMovieLists);

        movieList.setMovies(eligibleMovieMovieLists);
        return PojoEntityParamDtoConverter.convertMovieListEntityToPojo(movieList, eligibleMovies.stream().toList());
    }

    @Override
    public String deleteMovieList(int id, User user) {
        return null;
    }

    @Override
    public MovieListPojo updateMovieList(MovieListUpdateParam movieListParam, User user) {
        return null;
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
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }
}
