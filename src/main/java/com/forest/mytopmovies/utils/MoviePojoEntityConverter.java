package com.forest.mytopmovies.utils;

import com.forest.mytopmovies.entity.Genre;
import com.forest.mytopmovies.pojos.Movie;
import com.forest.mytopmovies.service.movie.GenreService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MoviePojoEntityConverter {

    private final GenreService genreService;

    public MoviePojoEntityConverter(GenreService genreService) {
        this.genreService = genreService;
    }

    public com.forest.mytopmovies.entity.Movie convertMoviePojoToEntity(Movie moviePojo) {
        return com.forest.mytopmovies.entity.Movie.builder()
                .id(moviePojo.getId())
                .originalTitle(moviePojo.getOriginal_title())
                .title(moviePojo.getTitle())
                .originalLanguage(moviePojo.getOriginal_language())
                .overview(moviePojo.getOverview())
                .averageVote(moviePojo.getVote_average())
                .releaseDate(moviePojo.getRelease_date())
                .genres(getGenreList(moviePojo.getGenre_ids()))
                .build();
    }

    public List<com.forest.mytopmovies.entity.Movie> transferMoviePojoListToEntityList(List<Movie> movies) {
        return movies.stream()
                .map(this::convertMoviePojoToEntity)
                .toList();
    }

    private List<Genre> getGenreList(List<Integer> genreIds) {
        List<Genre> genreList = new ArrayList<>();
        if (genreIds != null) {
            genreList = genreIds.stream()
                    .map(genreService::findGenreByTMDBId)
                    .filter(Optional::isPresent)
                    .map(Optional::get).toList();
        }
        return genreList;
    }
}
