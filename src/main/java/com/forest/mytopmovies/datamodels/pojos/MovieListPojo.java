package com.forest.mytopmovies.datamodels.pojos;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Data
@Jacksonized
public class MovieListPojo {
    private final int id;

    private final String movieListName;

    private final String description;

    private final List<MoviePojo> movies;

}
