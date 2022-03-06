package com.forest.mytopmovies.datamodels.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@Jacksonized
public class GenreDto {
    private final int id;

    private final String name;
}
