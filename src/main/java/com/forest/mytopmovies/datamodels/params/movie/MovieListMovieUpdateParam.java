package com.forest.mytopmovies.datamodels.params.movie;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public record MovieListMovieUpdateParam(
        @NotNull @NotBlank Integer id,
        List<Integer> movies) {
}
