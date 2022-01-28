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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.ok;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class ProtectedMovieListControllerIT extends IntegrationTest {
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
    void canCreateMovieList() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        String token = registerUser(mapper, "forest1", "123456");

        String movieListName = "list1";
        String description = "test-description";
        Integer[] movieIds = {550};
        String movieListJson = mapper.writeValueAsString(defaultMovieList(movieListName, description, movieIds));
        String expectedResponse = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/mtm/createMovieList.json");
        String expectedTMDBResponse = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/tmdb/searchMovieById.json");

        whenHttp(server)
                .match(Condition.endsWithUri("/movie/550"), Condition.parameter("api_key", "test-key"))
                .then(ok(), stringContent(expectedTMDBResponse));

        // when
        MvcResult result = this.mockMvc.perform(post("/protected/movielist")
                        .header("Authorization", String.format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON).content(movieListJson))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        // then
        JSONAssert.assertEquals(result.getResponse().getContentAsString(), expectedResponse, JSONCompareMode.LENIENT);
    }

    @Test
    void deleteMovieList() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        String token = registerUser(mapper, "forest2", "123456");

        String movieListName = "test1";
        String description = "test-description";
        Integer[] movieIds = {550};

        String expectedTMDBResponse = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/tmdb/searchMovieById.json");

        whenHttp(server)
                .match(Condition.endsWithUri("/movie/550"), Condition.parameter("api_key", "test-key"))
                .then(ok(), stringContent(expectedTMDBResponse));

        String movieListResponse = createMovieList(mapper, movieListName, description, movieIds, token);
        MovieListPojo movieListPojo = mapper.readValue(movieListResponse, MovieListPojo.class);

        // when
        MvcResult response = this.mockMvc.perform(delete("/protected/movielist/" + movieListPojo.getId())
                        .header("Authorization", String.format("Bearer %s", token)))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        // then
        assertThat(response.getResponse().getContentAsString()).isEqualTo("Successfully deleted movie list with id " + movieListPojo.getId());
    }

    @Test
    void updateMovieList() {
    }

    @Test
    void getMovieList() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        String token = registerUser(mapper, "forest4", "123456");

        String movieListName = "test1";
        String description = "test-description";
        Integer[] movieIds = {550};

        String expectedTMDBResponse = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/json_response/tmdb/searchMovieById.json");

        whenHttp(server)
                .match(Condition.endsWithUri("/movie/550"), Condition.parameter("api_key", "test-key"))
                .then(ok(), stringContent(expectedTMDBResponse));

        createMovieList(mapper, movieListName, description, movieIds, token);

        // when
        MvcResult response = this.mockMvc.perform(get("/protected/movielist")
                        .header("Authorization", String.format("Bearer %s", token)))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();
        PagePojo<MovieListPojo> pojoRes = mapper.readValue(response.getResponse().getContentAsString(), new TypeReference<>() {
        });

        // then
        assertThat(pojoRes.getResults().get(0).getMovieListName()).isEqualTo(movieListName);
        assertThat(pojoRes.getResults().get(0).getDescription()).isEqualTo(description);
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

    private String registerUser(ObjectMapper mapper, String username, String password) throws Exception {
        String userJson = mapper.writeValueAsString(defaultUser(username, password));
        MvcResult registerRes = this.mockMvc.perform(post("/public/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();
        return registerRes.getResponse().getContentAsString();
    }

    private String createMovieList(ObjectMapper mapper, String movieListName, String description, Integer[] movieIds, String token) throws Exception {
        String movieListJson = mapper.writeValueAsString(defaultMovieList(movieListName, description, movieIds));

        MvcResult result = this.mockMvc.perform(post("/protected/movielist")
                        .header("Authorization", String.format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON).content(movieListJson))
                .andDo(print()).andReturn();
        return result.getResponse().getContentAsString();
    }
}