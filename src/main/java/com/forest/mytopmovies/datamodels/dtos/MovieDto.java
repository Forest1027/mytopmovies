package com.forest.mytopmovies.datamodels.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDto {
    private String poster_path;

    private Boolean adult;

    private String overview;

    private Date release_date;

    private List<Integer> genre_ids;

    private List<GenreDto> genres;

    private Integer id;

    private String original_title;

    private String original_language;

    private String title;

    private String backdrop_path;

    private Double popularity;

    private Integer vote_count;

    private Boolean video;

    private Double vote_average;

}
