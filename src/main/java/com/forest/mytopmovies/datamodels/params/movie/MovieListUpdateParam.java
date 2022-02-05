package com.forest.mytopmovies.datamodels.params.movie;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public record MovieListUpdateParam(
        @NotNull(message = "id cannot be empty") @NotBlank(message = "id cannot be empty") Integer id,
        @NotNull(message = "movieListName cannot be empty") @NotBlank(message = "movieListName cannot be empty") String movieListName,
        String description,
        List<Integer> movies
) {
}
