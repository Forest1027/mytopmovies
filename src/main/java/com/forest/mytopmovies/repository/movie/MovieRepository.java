package com.forest.mytopmovies.repository.movie;

import com.forest.mytopmovies.datamodels.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Optional<Movie> findByTmdbId(int tmdbId);

}
