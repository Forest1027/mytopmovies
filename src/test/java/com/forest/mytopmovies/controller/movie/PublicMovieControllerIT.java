package com.forest.mytopmovies.controller.movie;

import com.forest.utils.FileReaderUtil;
import com.forest.utils.IntegrationTest;
import com.xebialabs.restito.semantics.Condition;
import com.xebialabs.restito.server.StubServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.ok;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class PublicMovieControllerIT extends IntegrationTest {

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
        String expectedReponse = FileReaderUtil.readJsonFromFile("src/test/java/com/forest/utils/tmdb/json_response/search.json");
//        String uri = "http://localhost:9999/search/movie?api_key=test-key&page=1&query=" + UriUtils.encode(movieName, "UTF-8");
        String uri = "/search/movie";
        whenHttp(server)
                .match(Condition.endsWithUri(uri), Condition.parameter("api_key", "test-key"))
                .then(ok(), stringContent(expectedReponse));

        // when

        // then
        this.mockMvc.perform(get("/public/movies?movieName=" + movieName))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
}