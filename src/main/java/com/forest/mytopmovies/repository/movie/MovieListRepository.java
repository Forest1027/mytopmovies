package com.forest.mytopmovies.repository.movie;

import com.forest.mytopmovies.entity.MovieList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieListRepository extends JpaRepository<MovieList, Integer> {

    Page<MovieList> findAllByUserId(String userId, Pageable pageable);

    Page<MovieList> findAllByUserIdAndMovieListName(String userId, String movieListName, Pageable pageable);

}
