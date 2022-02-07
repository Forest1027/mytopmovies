package com.forest.mytopmovies.datamodels.pojos;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@Jacksonized
public class GenrePojo {
    private final int tmdbId;

    private final String genreName;

}
