package com.forest.mytopmovies.datamodels.params.movie;

import javax.validation.constraints.NotNull;
import java.util.List;

public record MovieListMovieUpdateParam(
        @NotNull(message = "id cannot be empty") Integer id,
        List<Integer> movies
) {
}
