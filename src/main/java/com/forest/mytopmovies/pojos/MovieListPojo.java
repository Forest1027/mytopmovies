package com.forest.mytopmovies.pojos;

import com.forest.mytopmovies.entity.MovieMovieList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieListPojo {
    private int id;

    private String movieListName;

    private String description;

    private Set<MovieMovieList> movies;

}
