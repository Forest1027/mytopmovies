package com.forest.mytopmovies.datamodels.params.movie;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record MovieListUpdateParam(
        @NotNull @NotBlank Integer id,
        @NotNull @NotBlank String movieListName,
        String description
) {
}
