package com.forest.mytopmovies.utils;

import com.forest.mytopmovies.constants.TMDBConstants;
import com.forest.mytopmovies.entity.Movie;
import com.forest.mytopmovies.service.movie.GenreService;
import com.forest.utils.FileReaderUtil;
import com.forest.utils.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

class ExternalMovieDBApiUtilUnitTest extends UnitTest {

    private ExternalMovieDBApiUtil underTest;

    private TMDBConstants tmdbConstants;

    private MoviePojoEntityConverter moviePojoEntityConverter;

    @Mock
    private GenreService genreService;

    @BeforeEach
    void setUp() {
        moviePojoEntityConverter = new MoviePojoEntityConverter(genreService);
        tmdbConstants = new TMDBConstants();
        tmdbConstants.baseUrl = "http://localhost:9999";
        tmdbConstants.searchMovieAPI = "/search/movie";
        underTest = new ExternalMovieDBApiUtil(moviePojoEntityConverter, tmdbConstants);
    }

    @Test
    void canSearchMovies() throws IOException, ParseException {
        // given
        String expectedResponse = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/tmdb/search.json");
        double expectedVoteAvg = 7.3;
        String expectedOriginalTitle = "Don't Look Up";
        String date = "2021-12-07";
        Date expectedReleaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);

        try(MockedStatic<HttpUtil> utilities = Mockito.mockStatic(HttpUtil.class)) {
            utilities.when(() -> HttpUtil.get(anyString(), anyString())).thenReturn(expectedResponse);

            // when
            List<Movie> results = underTest.searchMovies(anyString(), anyInt()).getResults();

            // then
            Movie result = results.get(0);
            assertThat(result.getAverageVote()).isEqualTo(expectedVoteAvg);
            assertThat(result.getOriginalTitle()).isEqualTo(expectedOriginalTitle);
            assertThat(result.getReleaseDate()).isEqualTo(expectedReleaseDate);
        }
    }
}