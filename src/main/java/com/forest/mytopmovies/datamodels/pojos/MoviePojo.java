package com.forest.mytopmovies.datamodels.pojos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;
import java.util.List;

@Builder
@Data
@Jacksonized
public class MoviePojo {
    private final int tmdbId;

    private final String originalTitle;

    private final String title;

    private final String originalLanguage;

    private final String overview;

    private final double averageVote;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final Date releaseDate;

    private final List<GenrePojo> genres;

}
