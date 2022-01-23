package com.forest.mytopmovies.service.movie.impl;

import com.forest.mytopmovies.entity.MovieList;
import com.forest.mytopmovies.entity.User;
import com.forest.mytopmovies.params.movie.MovieListParam;
import com.forest.mytopmovies.params.movie.MovieListUpdateParam;
import com.forest.mytopmovies.pojos.MovieListPojo;
import com.forest.mytopmovies.pojos.Page;
import com.forest.mytopmovies.repository.movie.MovieListRepository;
import com.forest.mytopmovies.service.movie.MovieListService;
import com.forest.mytopmovies.utils.PojoEntityParamConverter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class MovieListServiceImpl implements MovieListService {

    private final MovieListRepository movieListRepository;

    @Override
    public MovieList createMovieList(MovieListParam movieListParam, User user) {
        MovieList movieList = PojoEntityParamConverter.convertMovieListParamToEntity(movieListParam);
        movieList.setUser(user);
        return movieListRepository.saveAndFlush(movieList);
    }

    @Override
    public String deleteMovieList(int id, User user) {
        return null;
    }

    @Override
    public MovieList updateMovieList(MovieListUpdateParam movieListParam, User user) {
        return null;
    }

    @Override
    public Page<MovieListPojo> getMovieLists(String name, Integer page, User user) {
        if (page == null) page = 1;
        Pageable pageable = Pageable.ofSize(5).withPage(page - 1);
        org.springframework.data.domain.Page<MovieList> queryRes;
        if (name == null) {
            queryRes = movieListRepository.findAllByUserId(user.getId(), pageable);
        } else {
            queryRes = movieListRepository.findAllByUserIdAndMovieListName(user.getId(), name, pageable);
        }
        return Page.<MovieListPojo>builder()
                .total_pages(queryRes.getTotalPages())
                .total_results(queryRes.getNumberOfElements())
                .page(page)
                .results(queryRes.getContent().stream().map(PojoEntityParamConverter::convertMovieListEntityToPojo).toList()).build();
    }
}
