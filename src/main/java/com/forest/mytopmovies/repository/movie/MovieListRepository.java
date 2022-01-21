package com.forest.mytopmovies.repository.movie;

import com.forest.mytopmovies.entity.MovieList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieListRepository extends JpaRepository<MovieList, Integer> {

}
