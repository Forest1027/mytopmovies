package com.forest.mytopmovies.repository.movie;

import com.forest.mytopmovies.datamodels.entity.MovieList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieListRepository extends JpaRepository<MovieList, Integer> {

    Page<MovieList> findAllByUserId(String userId, Pageable pageable);

    Page<MovieList> findAllByUserIdAndMovieListName(String userId, String movieListName, Pageable pageable);

    Optional<MovieList> findByUserIdAndId(String userId, int id);

}
