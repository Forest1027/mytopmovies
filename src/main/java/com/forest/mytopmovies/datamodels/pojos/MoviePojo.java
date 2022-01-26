package com.forest.mytopmovies.datamodels.pojos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoviePojo {

    private int tmdbId;

    private String originalTitle;

    private String title;

    private String originalLanguage;

    private String overview;

    private double averageVote;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;

    private List<GenrePojo> genres;


}
