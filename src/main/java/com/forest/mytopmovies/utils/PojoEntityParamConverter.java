package com.forest.mytopmovies.utils;

import com.forest.mytopmovies.entity.Genre;
import com.forest.mytopmovies.entity.Movie;
import com.forest.mytopmovies.entity.MovieList;
import com.forest.mytopmovies.entity.MovieMovieList;
import com.forest.mytopmovies.params.movie.MovieListParam;
import com.forest.mytopmovies.service.movie.GenreService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PojoEntityParamConverter {
    private PojoEntityParamConverter() {
    }

    public static MovieList convertMovieListParamToEntity(MovieListParam movieListParam) {
        Set<MovieMovieList> movies = new HashSet<>();
        if (movieListParam.movies() != null) {
            movies = movieListParam.movies().stream().map(id -> MovieMovieList.builder().movie(Movie.builder().tmdbId(id).build()).build()).collect(Collectors.toSet());
        }
        return MovieList.builder()
                .movieListName(movieListParam.movieListName())
                .description(movieListParam.description())
                .movies(movies)
                .build();
    }

    public static com.forest.mytopmovies.entity.Movie convertMoviePojoToEntity(com.forest.mytopmovies.pojos.Movie moviePojo, GenreService genreService) {
        return com.forest.mytopmovies.entity.Movie.builder()
                .id(moviePojo.getId())
                .originalTitle(moviePojo.getOriginal_title())
                .title(moviePojo.getTitle())
                .originalLanguage(moviePojo.getOriginal_language())
                .overview(moviePojo.getOverview())
                .averageVote(moviePojo.getVote_average())
                .releaseDate(moviePojo.getRelease_date())
                .genres(getGenreList(moviePojo.getGenre_ids(), genreService))
                .build();
    }

    public static List<Movie> transferMoviePojoListToEntityList(List<com.forest.mytopmovies.pojos.Movie> movies, GenreService genreService) {
        return movies.stream()
                .map(movie -> PojoEntityParamConverter.convertMoviePojoToEntity(movie, genreService))
                .toList();
    }

    private static List<Genre> getGenreList(List<Integer> genreIds, GenreService genreService) {
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
