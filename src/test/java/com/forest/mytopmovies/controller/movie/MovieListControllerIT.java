package com.forest.mytopmovies.controller.movie;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forest.mytopmovies.datamodels.entity.User;
import com.forest.mytopmovies.datamodels.params.movie.MovieListMovieUpdateParam;
import com.forest.mytopmovies.datamodels.params.movie.MovieListParam;
import com.forest.mytopmovies.datamodels.params.movie.MovieListUpdateParam;
import com.forest.mytopmovies.datamodels.pojos.MovieListPojo;
import com.forest.mytopmovies.datamodels.pojos.PagePojo;
import com.forest.utils.FileReaderUtil;
import com.forest.utils.IntegrationTest;
import com.xebialabs.restito.semantics.Condition;
import com.xebialabs.restito.server.StubServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.ok;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class MovieListControllerIT extends IntegrationTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MovieListControllerIT.class);

    private static String password = "123456";

    private static String baseMovieListName = "list";

    private static String baseDescription = "test-description";

    private static Integer[] baseMovieIds = {550};

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
    @Order(1)
    void canCreateMovieList() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        String token = registerUser(mapper, "forest1", password);

        String movieListJson = mapper.writeValueAsString(buildMovieList(baseMovieListName, baseDescription, baseMovieIds));
        String expectedResponse = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/mtm/createMovieList.json");
        String expectedTMDBResponse = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/tmdb/searchMovieById.json");

        whenHttp(server)
                .match(Condition.endsWithUri("/movie/550"), Condition.parameter("api_key", "test-key"))
                .then(ok(), stringContent(expectedTMDBResponse));

        // when
        MvcResult result = this.mockMvc.perform(post("/api/v1/movielist")
                        .header("Authorization", String.format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON).content(movieListJson))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        // then
        JSONAssert.assertEquals(result.getResponse().getContentAsString(), expectedResponse, JSONCompareMode.LENIENT);
    }


    @Test
    @Order(2)
    void canUpdateMovieList() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        String token = registerUser(mapper, "forest2", password);

        String movieListNameUpdate = "test1Update";
        String descriptionUpdate = "test-descriptionUpdate";
        Integer[] movieIdsUpdate = {550, 551};

        String expectedTMDBResponse1 = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/tmdb/searchMovieById.json");
        String expectedTMDBResponse2 = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/tmdb/searchMovieById2.json");

        whenHttp(server)
                .match(Condition.endsWithUri("/movie/550"), Condition.parameter("api_key", "test-key"))
                .then(ok(), stringContent(expectedTMDBResponse1));

        whenHttp(server)
                .match(Condition.endsWithUri("/movie/551"), Condition.parameter("api_key", "test-key"))
                .then(ok(), stringContent(expectedTMDBResponse2));

        String movieListResponse = createMovieList(mapper, baseMovieListName, baseDescription, baseMovieIds, token);
        MovieListPojo movieListPojo = mapper.readValue(movieListResponse, MovieListPojo.class);

        MovieListUpdateParam movieListParam = new MovieListUpdateParam(movieListPojo.getId(), movieListNameUpdate, descriptionUpdate, Arrays.stream(movieIdsUpdate).toList());
        String movieListJson = mapper.writeValueAsString(movieListParam);

        // when
        MvcResult response = this.mockMvc.perform(put("/api/v1/movielist")
                        .header("Authorization", String.format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON).content(movieListJson))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        // then
        MovieListPojo responsePojo = mapper.readValue(response.getResponse().getContentAsString(), MovieListPojo.class);
        assertThat(responsePojo.getMovieListName()).isEqualTo(movieListNameUpdate);
        assertThat(responsePojo.getDescription()).isEqualTo(descriptionUpdate);
        assertThat(responsePojo.getMovies()).hasSize(movieIdsUpdate.length);
    }

    @Test
    @Order(3)
    void canGetMovieListById() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        String token = registerUser(mapper, "forest3", "123456");

        String expectedTMDBResponse = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/tmdb/searchMovieById.json");

        whenHttp(server)
                .match(Condition.endsWithUri("/movie/550"), Condition.parameter("api_key", "test-key"))
                .then(ok(), stringContent(expectedTMDBResponse));

        String movieListResponse = createMovieList(mapper, baseMovieListName, baseDescription, baseMovieIds, token);
        MovieListPojo movieListPojo = mapper.readValue(movieListResponse, MovieListPojo.class);

        // when
        MvcResult response = this.mockMvc.perform(get("/api/v1/movielist/" + movieListPojo.getId())
                        .header("Authorization", String.format("Bearer %s", token)))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        // then
        MovieListPojo responsePojo = mapper.readValue(response.getResponse().getContentAsString(), MovieListPojo.class);
        assertThat(responsePojo.getMovieListName()).isEqualTo(baseMovieListName);
        assertThat(responsePojo.getDescription()).isEqualTo(baseDescription);
        assertThat(responsePojo.getMovies()).hasSize(baseMovieIds.length);
    }

    @Test
    @Order(4)
    void canAddMoviesToMovieList() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        String token = registerUser(mapper, "forest4", "123456");

        Integer[] movieIdsUpdate = {551};

        String expectedTMDBResponse1 = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/tmdb/searchMovieById.json");
        String expectedTMDBResponse2 = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/tmdb/searchMovieById2.json");

        whenHttp(server)
                .match(Condition.endsWithUri("/movie/550"), Condition.parameter("api_key", "test-key"))
                .then(ok(), stringContent(expectedTMDBResponse1));

        whenHttp(server)
                .match(Condition.endsWithUri("/movie/551"), Condition.parameter("api_key", "test-key"))
                .then(ok(), stringContent(expectedTMDBResponse2));

        String movieListResponse = createMovieList(mapper, baseMovieListName, baseDescription, baseMovieIds, token);
        MovieListPojo movieListPojo = mapper.readValue(movieListResponse, MovieListPojo.class);

        MovieListMovieUpdateParam movieListParam = new MovieListMovieUpdateParam(movieListPojo.getId(), Arrays.stream(movieIdsUpdate).toList());
        String movieListJson = mapper.writeValueAsString(movieListParam);

        // when
        MvcResult response = this.mockMvc.perform(put("/api/v1/movielist/movies")
                        .header("Authorization", String.format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON).content(movieListJson))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        // then
        MovieListPojo responsePojo = mapper.readValue(response.getResponse().getContentAsString(), MovieListPojo.class);
        assertThat(responsePojo.getMovies()).hasSize(movieIdsUpdate.length + baseMovieIds.length);
    }

    @Test
    @Order(5)
    void canDeleteMovieList() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        String token = registerUser(mapper, "forest5", "123456");

        String expectedTMDBResponse = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/tmdb/searchMovieById.json");

        whenHttp(server)
                .match(Condition.endsWithUri("/movie/550"), Condition.parameter("api_key", "test-key"))
                .then(ok(), stringContent(expectedTMDBResponse));

        String movieListResponse = createMovieList(mapper, baseMovieListName, baseDescription, baseMovieIds, token);
        MovieListPojo movieListPojo = mapper.readValue(movieListResponse, MovieListPojo.class);

        // when
        MvcResult response = this.mockMvc.perform(delete("/api/v1/movielist/" + movieListPojo.getId())
                        .header("Authorization", String.format("Bearer %s", token)))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        // then
        assertThat(response.getResponse().getContentAsString()).isEqualTo("Successfully deleted movie list with id " + movieListPojo.getId());
    }

    @Test
    @Order(6)
    void cantDeleteMovieList() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        String token = registerUser(mapper, "forest6", "123456");

        String expectedTMDBResponse = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/tmdb/searchMovieById.json");

        whenHttp(server)
                .match(Condition.endsWithUri("/movie/550"), Condition.parameter("api_key", "test-key"))
                .then(ok(), stringContent(expectedTMDBResponse));

        String movieListResponse = createMovieList(mapper, baseMovieListName, baseDescription, baseMovieIds, token);
        MovieListPojo movieListPojo = mapper.readValue(movieListResponse, MovieListPojo.class);
        int passedId = movieListPojo.getId() + 1;

        // when

        // then
        this.mockMvc.perform(delete("/api/v1/movielist/" + passedId)
                        .header("Authorization", String.format("Bearer %s", token)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Movie list with id " + passedId + " is not found.")))
                .andReturn();
    }

    @Test
    @Order(7)
    void cantGetMovieListByIdWithoutToken() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        String token = registerUser(mapper, "forest3", "123456");

        String expectedTMDBResponse = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/tmdb/searchMovieById.json");

        whenHttp(server)
                .match(Condition.endsWithUri("/movie/550"), Condition.parameter("api_key", "test-key"))
                .then(ok(), stringContent(expectedTMDBResponse));

        String movieListResponse = createMovieList(mapper, baseMovieListName, baseDescription, baseMovieIds, token);
        MovieListPojo movieListPojo = mapper.readValue(movieListResponse, MovieListPojo.class);

        // when
        MvcResult result = this.mockMvc.perform(get("/api/v1/movielist/" + movieListPojo.getId()))
                .andDo(print())
                .andExpect(status().isUnauthorized()).andReturn();

        // then
        assertThat(result.getResponse().getContentAsString()).contains("Missing authentication token");
    }

    @Test
    @Order(8)
    void cantGetMovieListByIdWithExpiredToken() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        String token = registerUser(mapper, "forest3", "123456");

        String expectedTMDBResponse = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/tmdb/searchMovieById.json");

        whenHttp(server)
                .match(Condition.endsWithUri("/movie/550"), Condition.parameter("api_key", "test-key"))
                .then(ok(), stringContent(expectedTMDBResponse));

        String movieListResponse = createMovieList(mapper, baseMovieListName, baseDescription, baseMovieIds, token);
        MovieListPojo movieListPojo = mapper.readValue(movieListResponse, MovieListPojo.class);
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        // when

        // then
        LOGGER.info("Waiting for 25 seconds for assertion.");
        executorService.schedule(() -> {
            try {
                assertRequestWithExpiredToken(movieListPojo, token);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 25, TimeUnit.SECONDS);
        executorService.shutdown();
        executorService.awaitTermination(26, TimeUnit.SECONDS);
    }

    @Test
    @Order(9)
    void canGetAllMovieLists() throws Exception {
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
        MvcResult response = this.mockMvc.perform(get("/api/v1/movielist")
                        .header("Authorization", String.format("Bearer %s", token)))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();
        PagePojo<MovieListPojo> pojoRes = mapper.readValue(response.getResponse().getContentAsString(), new TypeReference<>() {
        });

        // then
        assertThat(pojoRes.getResults().get(0).getMovieListName()).isEqualTo(movieListName);
        assertThat(pojoRes.getResults().get(0).getDescription()).isEqualTo(description);
    }

    private void assertRequestWithExpiredToken(MovieListPojo movieListPojo, String token) throws Exception {
        MvcResult result = this.mockMvc.perform(get("/api/v1/movielist/" + movieListPojo.getId())
                        .header("Authorization", String.format("Bearer %s", token)))
                .andDo(print())
                .andExpect(status().isUnauthorized()).andReturn();
        assertThat(result.getResponse().getContentAsString()).contains("JWT expired");
    }

    private static User defaultUser(String username, String password) {
        return User.builder()
                .username(username)
                .password(password).build();
    }

    private static MovieListParam buildMovieList(String movieListName, String description, Integer[] movieIds) {
        List<Integer> movies = new ArrayList<>(Arrays.asList(movieIds));
        return new MovieListParam(movieListName, description, movies);
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
        String movieListJson = mapper.writeValueAsString(buildMovieList(movieListName, description, movieIds));

        MvcResult result = this.mockMvc.perform(post("/api/v1/movielist")
                        .header("Authorization", String.format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON).content(movieListJson))
                .andDo(print()).andReturn();
        return result.getResponse().getContentAsString();
    }
}