package com.forest.mytopmovies.service.movie.impl;

import com.forest.mytopmovies.entity.MovieList;
import com.forest.mytopmovies.params.movie.MovieListParam;
import com.forest.mytopmovies.repository.movie.MovieListRepository;
import com.forest.mytopmovies.service.movie.MovieListService;
import com.forest.mytopmovies.utils.PojoEntityParamConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class MovieListServiceImpl implements MovieListService {

    private final MovieListRepository movieListRepository;

    @Override
    public MovieList createMovieList(MovieListParam movieListParam) {
        MovieList movieList = PojoEntityParamConverter.convertMovieListParamToEntity(movieListParam);
        return movieListRepository.saveAndFlush(movieList);
    }
}
