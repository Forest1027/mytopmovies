package com.forest.mytopmovies.datamodels.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;
import java.util.List;

@Builder
@Data
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDto {
    private final String poster_path;

    private final Boolean adult;

    private final String overview;

    private final Date release_date;

    private final List<Integer> genre_ids;

    private final List<GenreDto> genres;

    private final Integer id;

    private final String original_title;

    private final String original_language;

    private final String title;

    private final String backdrop_path;

    private final Double popularity;

    private final Integer vote_count;

    private final Boolean video;

    private final Double vote_average;

}
