package com.forest.mytopmovies.utils;

import com.forest.mytopmovies.pojos.MoviePojo;
import com.forest.mytopmovies.service.movie.GenreService;
import com.forest.utils.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class PojoEntityConverterUnitTest extends UnitTest {


    @Mock
    private GenreService genreService;

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

        MoviePojo moviePojo = MoviePojo.builder().id(id)
                .original_title(originalTitle)
                .title(title)
                .original_language(originalLanguage)
                .overview(overview)
                .vote_average(avgVote)
                .release_date(releaseDate)
                .build();

        // when
        com.forest.mytopmovies.entity.Movie result = PojoEntityParamConverter.convertMoviePojoToEntity(moviePojo, genreService);

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