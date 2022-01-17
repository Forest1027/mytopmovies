package com.forest.mytopmovies.utils;

import com.forest.mytopmovies.pojos.Movie;
import com.forest.utils.UnitTest;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class MoviePojoEntityConverterUnitTest extends UnitTest {

    @Test
    void canConvertMoviePojoToEntity() {
        // given
        int id = 1;
        String originalTitle = "original title";
        String title = "title";
        String originalLanguage = "en";
        String overview = "overview";
        double avgVote = 7.3;
        Date releaseDate = new Date();

        Movie moviePojo = Movie.builder().id(id)
                .original_title(originalTitle)
                .title(title)
                .original_language(originalLanguage)
                .overview(overview)
                .vote_average(avgVote)
                .release_date(releaseDate)
                .build();

        // when
        com.forest.mytopmovies.entity.Movie result = MoviePojoEntityConverter.convertMoviePojoToEntity(moviePojo);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getOriginalTitle()).isEqualTo(originalTitle);
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getOriginalLanguage()).isEqualTo(originalLanguage);
        assertThat(result.getOverview()).isEqualTo(overview);
        assertThat(result.getAverageVote()).isEqualTo(avgVote);
        assertThat(result.getReleaseDate()).isEqualTo(releaseDate);
    }

}