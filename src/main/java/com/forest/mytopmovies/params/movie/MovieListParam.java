package com.forest.mytopmovies.params.movie;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public record MovieListParam(
        @NotNull @NotBlank String movieListName,
        String description,
        List<Integer> movies
) {

}
