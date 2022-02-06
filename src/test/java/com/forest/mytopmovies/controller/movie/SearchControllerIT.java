package com.forest.mytopmovies.controller.movie;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forest.mytopmovies.datamodels.entity.User;
import com.forest.mytopmovies.datamodels.params.movie.MovieListParam;
import com.forest.mytopmovies.datamodels.pojos.MovieListPojo;
import com.forest.mytopmovies.datamodels.pojos.PagePojo;
import com.forest.utils.FileReaderUtil;
import com.forest.utils.IntegrationTest;
import com.xebialabs.restito.semantics.Condition;
import com.xebialabs.restito.server.StubServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.builder.verify.VerifyHttp.verifyHttp;
import static com.xebialabs.restito.semantics.Action.ok;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class SearchControllerIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private StubServer server;

    @BeforeEach
    public void start() {
        server = new StubServer(9999);
        server.start();
    }

    @AfterEach
    public void stop() {
        server.stop();
    }

    @Test
    void canSearchMovieByNameWithDefaultPage() throws Exception {
        // given
        String movieName = "Don't look up";
        String expectedResponse = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/tmdb/search.json");
        String expectedResponseMTM = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/mtm/searchMovie.json");
        String uri = "/search/movie";
        whenHttp(server)
                .match(Condition.endsWithUri(uri), Condition.parameter("api_key", "test-key"))
                .then(ok(), stringContent(expectedResponse));

        // when
        MvcResult result = this.mockMvc.perform(get("/api/v1/search/movies?query=" + movieName))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // then
        verifyHttp(server).once(Condition.endsWithUri(uri), Condition.parameter("api_key", "test-key"));
        JSONAssert.assertEquals(result.getResponse().getContentAsString(), expectedResponseMTM, JSONCompareMode.LENIENT);
    }

    @Test
    void canSearchMovieByNameWithPassedPage() throws Exception {
        // given
        String movieName = "Don't look up";
        String expectedResponse = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/tmdb/search.json");
        String expectedResponseMTM = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/mtm/searchMovie.json");
        String uri = "/search/movie";
        int page = 1;
        whenHttp(server)
                .match(Condition.endsWithUri(uri), Condition.parameter("api_key", "test-key"))
                .then(ok(), stringContent(expectedResponse));

        // when
        MvcResult result = this.mockMvc.perform(get("/api/v1/search/movies?query=" + movieName+"&page="+page))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // then
        verifyHttp(server).once(Condition.endsWithUri(uri), Condition.parameter("api_key", "test-key"));
        JSONAssert.assertEquals(result.getResponse().getContentAsString(), expectedResponseMTM, JSONCompareMode.LENIENT);
    }

    @Test
    void canThrowTMDBExceptionWhenFailure() throws Exception {
        // given
        String movieName = "Don't look up";
        String expectedReponse = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/tmdb/search.json");
        String uri = "/search/movie";
        whenHttp(server)
                .match(Condition.endsWithUri(uri), Condition.parameter("api_key", "test-wrong-key"))
                .then(ok(), stringContent(expectedReponse));

        // when

        // then
        this.mockMvc.perform(get("/api/v1/search/movies?query=" + movieName))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Failure when execute request to TMDB API")))
                .andReturn();
    }

    @Test
    void canSearchMovieListByName() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        String token = registerUser(mapper, "forest999", "123456");

        String movieListName = "test1";
        String description = "test-description";
        Integer[] movieIds = {550};

        String expectedTMDBResponse = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/tmdb/searchMovieById.json");

        whenHttp(server)
                .match(Condition.endsWithUri("/movie/550"), Condition.parameter("api_key", "test-key"))
                .then(ok(), stringContent(expectedTMDBResponse));

        createMovieList(mapper, movieListName, description, movieIds, token);

        // when
        MvcResult response = this.mockMvc.perform(get("/api/v1/search/movielists?query="+movieListName)
                        .header("Authorization", String.format("Bearer %s", token)))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();
        PagePojo<MovieListPojo> pojoRes = mapper.readValue(response.getResponse().getContentAsString(), new TypeReference<>() {
        });

        // then
        assertThat(pojoRes.getResults().get(0).getMovieListName()).isEqualTo(movieListName);
        assertThat(pojoRes.getResults().get(0).getDescription()).isEqualTo(description);
    }

    private String registerUser(ObjectMapper mapper, String username, String password) throws Exception {
        String userJson = mapper.writeValueAsString(defaultUser(username, password));
        MvcResult registerRes = this.mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();
        return registerRes.getResponse().getContentAsString();
    }

    private String createMovieList(ObjectMapper mapper, String movieListName, String description, Integer[] movieIds, String token) throws Exception {
        String movieListJson = mapper.writeValueAsString(defaultMovieList(movieListName, description, movieIds));

        MvcResult result = this.mockMvc.perform(post("/api/v1/movielist")
                        .header("Authorization", String.format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON).content(movieListJson))
                .andDo(print()).andReturn();
        return result.getResponse().getContentAsString();
    }

    private static User defaultUser(String username, String password) {
        return User.builder()
                .username(username)
                .password(password).build();
    }

    private static MovieListParam defaultMovieList(String movieListName, String description, Integer[] movieIds) {
        List<Integer> movies = new ArrayList<>(Arrays.asList(movieIds));
        return new MovieListParam(movieListName, description, movies);
    }
}