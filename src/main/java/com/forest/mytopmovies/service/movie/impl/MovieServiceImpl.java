package com.forest.mytopmovies.service.movie.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.forest.mytopmovies.datamodels.dtos.PageDto;
import com.forest.mytopmovies.datamodels.entity.Movie;
import com.forest.mytopmovies.datamodels.pojos.MoviePojo;
import com.forest.mytopmovies.datamodels.pojos.PagePojo;
import com.forest.mytopmovies.exceptions.TMDBHttpRequestException;
import com.forest.mytopmovies.service.movie.MovieService;
import com.forest.mytopmovies.utils.ExternalMovieDBApiUtil;
import com.forest.mytopmovies.utils.PojoEntityParamDtoConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {
    private ExternalMovieDBApiUtil externalMovieDBApiUtil;

    @Override
    public PagePojo<MoviePojo> searchTMDBMovieByName(String movieName, int page) throws TMDBHttpRequestException, JsonProcessingException {
        PageDto<Movie> moviePageDto = externalMovieDBApiUtil.searchMovies(movieName, page);
        return PagePojo.<MoviePojo>builder()
                .totalResults(moviePageDto.getTotal_results())
                .results(Optional.ofNullable(moviePageDto.getResults())
                        .map(Collection::stream).orElse(Stream.empty())
                        .map(PojoEntityParamDtoConverter::convertMovieEntityToPojo).toList())
                .totalPages(moviePageDto.getTotal_pages())
                .page(moviePageDto.getPage()).build();
    }

}
