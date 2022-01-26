package com.forest.mytopmovies.repository.movie;

import com.forest.mytopmovies.datamodels.entity.MovieMovieList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieMovieListRepository extends JpaRepository<MovieMovieList, Integer> {

    List<MovieMovieList> findAllByMovieListId(int id);

}
