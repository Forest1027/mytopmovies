package com.forest.mytopmovies.utils;

import com.forest.mytopmovies.datamodels.dtos.GenreDto;
import com.forest.mytopmovies.datamodels.dtos.MovieDto;
import com.forest.mytopmovies.datamodels.dtos.PageDto;
import com.forest.mytopmovies.datamodels.entity.Genre;
import com.forest.mytopmovies.datamodels.entity.Movie;
import com.forest.mytopmovies.datamodels.entity.MovieList;
import com.forest.mytopmovies.datamodels.entity.MovieMovieList;
import com.forest.mytopmovies.datamodels.params.movie.MovieListParam;
import com.forest.mytopmovies.datamodels.pojos.GenrePojo;
import com.forest.mytopmovies.datamodels.pojos.MovieListPojo;
import com.forest.mytopmovies.datamodels.pojos.MoviePojo;
import com.forest.mytopmovies.datamodels.pojos.PagePojo;
import com.forest.mytopmovies.service.movie.GenreService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PojoEntityParamDtoConverter {

    private PojoEntityParamDtoConverter() {
    }

    public static MovieList convertMovieListParamToEntity(MovieListParam movieListParam) {
        Set<MovieMovieList> movies = new HashSet<>();
        if (movieListParam.movies() != null) {
            movies = movieListParam.movies().stream().map(id -> MovieMovieList.builder().movieId(id).build()).collect(Collectors.toSet());
        }
        return MovieList.builder()
                .movieListName(movieListParam.movieListName())
                .description(movieListParam.description())
                .movies(movies)
                .build();
    }

    public static Movie convertMovieDtoToEntity(MovieDto movieDto, GenreService genreService) {
        List<Genre> genres;
        if (movieDto.getGenres() != null && !movieDto.getGenres().isEmpty()) {
            genres = movieDto.getGenres().stream().map(PojoEntityParamDtoConverter::convertGenreDtoToEntity).toList();
        } else {
            genres = getGenreList(movieDto.getGenre_ids(), genreService);
        }
        return Movie.builder()
                .id(movieDto.getId())
                .originalTitle(movieDto.getOriginal_title())
                .title(movieDto.getTitle())
                .originalLanguage(movieDto.getOriginal_language())
                .overview(movieDto.getOverview())
                .averageVote(movieDto.getVote_average())
                .releaseDate(movieDto.getRelease_date())
                .tmdbId(movieDto.getId())
                .genres(genres)
                .build();
    }

    public static Genre convertGenreDtoToEntity(GenreDto genreDto) {
        return Genre.builder().tmdbId(genreDto.getId()).genreName(genreDto.getName()).build();
    }

    public static List<Movie> convertMoviePojoListToEntityList(List<MovieDto> movies, GenreService genreService) {
        return movies.stream()
                .map(movie -> PojoEntityParamDtoConverter.convertMovieDtoToEntity(movie, genreService))
                .toList();
    }

    public static MovieListPojo convertMovieListEntityToPojo(MovieList movieList, List<Movie> movies) {
        return MovieListPojo.builder()
                .id(movieList.getId())
                .movieListName(movieList.getMovieListName())
                .description(movieList.getDescription())
                .movies(movies.stream().map(PojoEntityParamDtoConverter::convertMovieEntityToPojo).toList())
                .build();
    }

    public static MoviePojo convertMovieEntityToPojo(Movie movie) {
        return MoviePojo.builder()
                .tmdbId(movie.getTmdbId())
                .averageVote(movie.getAverageVote())
                .genres(Optional.ofNullable(movie.getGenres())
                        .map(Collection::stream).orElse(Stream.empty())
                        .map(PojoEntityParamDtoConverter::convertGenreEntityToPojo).toList())
                .originalTitle(movie.getOriginalTitle())
                .originalLanguage(movie.getOriginalLanguage())
                .overview(movie.getOverview())
                .releaseDate(movie.getReleaseDate())
                .title(movie.getTitle())
                .build();
    }

    public static GenrePojo convertGenreEntityToPojo(Genre genre) {
        return GenrePojo.builder()
                .tmdbId(genre.getTmdbId())
                .genreName(genre.getGenreName())
                .build();
    }

    public static <T> PagePojo<T> convertPageDtoToPojo(PageDto<T> pageDto) {
        return PagePojo.<T>builder()
                .page(pageDto.getPage())
                .totalPages(pageDto.getTotal_pages())
                .results(pageDto.getResults())
                .totalResults(pageDto.getTotal_results())
                .build();
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
