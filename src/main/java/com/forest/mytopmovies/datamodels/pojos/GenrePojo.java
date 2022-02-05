package com.forest.mytopmovies.datamodels.pojos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GenrePojo {
    private final int tmdbId;

    private final String genreName;

}
