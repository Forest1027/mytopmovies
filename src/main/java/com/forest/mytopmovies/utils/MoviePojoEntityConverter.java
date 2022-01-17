package com.forest.mytopmovies.utils;

import com.forest.mytopmovies.pojos.Movie;

import java.util.List;

public class MoviePojoEntityConverter {

    private MoviePojoEntityConverter() {
    }

    public static com.forest.mytopmovies.entity.Movie convertMoviePojoToEntity(Movie moviePojo) {
        return com.forest.mytopmovies.entity.Movie.builder()
                .id(moviePojo.getId())
                .originalTitle(moviePojo.getOriginal_title())
                .title(moviePojo.getTitle())
                .originalLanguage(moviePojo.getOriginal_language())
                .overview(moviePojo.getOverview())
                .averageVote(moviePojo.getVote_average())
                .releaseDate(moviePojo.getRelease_date())
                .build();
    }

    public static List<com.forest.mytopmovies.entity.Movie> transferMoviePojoListToEntityList(List<Movie> movies) {
        return movies.stream()
                .map(MoviePojoEntityConverter::convertMoviePojoToEntity)
                .toList();
    }
}
