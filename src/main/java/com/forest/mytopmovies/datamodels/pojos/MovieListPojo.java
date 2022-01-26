package com.forest.mytopmovies.datamodels.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieListPojo {
    private int id;

    private String movieListName;

    private String description;

    private List<MoviePojo> movies;

}
