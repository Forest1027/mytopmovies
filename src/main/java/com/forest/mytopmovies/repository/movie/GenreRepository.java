package com.forest.mytopmovies.repository.movie;

import com.forest.mytopmovies.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
    Optional<Genre> findOneByTMDBId(int tmdbId);
}
