package com.forest.mytopmovies.utils;

import com.forest.mytopmovies.exceptions.TMDBHttpRequestException;
import com.forest.utils.UnitTest;
import com.xebialabs.restito.server.StubServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.*;
import static com.xebialabs.restito.semantics.Condition.get;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;

class HttpUtilUnitTest extends UnitTest {

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
    void testGETRequestSuccess() {
        // given
        String expectedResponse = "Succeed";
        whenHttp(server)
                .match(get("/search/movie"))
                .then(ok(), stringContent(expectedResponse));

        // when
        String response = HttpUtil.get("http://localhost:9999", "/search/movie");

        // then
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    void testGETRequestFail() {
        // given
        whenHttp(server)
                .match("/search/movie")
                .then(status(HttpStatus.BAD_REQUEST_400));

        // when
        TMDBHttpRequestException exception = assertThrows(TMDBHttpRequestException.class, () -> HttpUtil.get("http://localhost:9999", "/search/movie"));


        // then
        assertThat(exception.getMessage()).contains("Failure when execute request to TMDB API");
    }

    @Test
    void testGETRequestNotFoundFail() {
        // given
        whenHttp(server)
                .match("/search/movie")
                .then(status(HttpStatus.NOT_FOUND_404));

        // when
        TMDBHttpRequestException exception = assertThrows(TMDBHttpRequestException.class, () -> HttpUtil.get("http://localhost:9999", "/search/movie"));


        // then
        assertThat(exception.getMessage()).contains("not found");
    }

}