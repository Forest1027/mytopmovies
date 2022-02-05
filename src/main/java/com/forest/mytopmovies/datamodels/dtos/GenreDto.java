package com.forest.mytopmovies.datamodels.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GenreDto {
    private final int id;

    private final String name;
}
