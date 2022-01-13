package com.forest.mytopmovies.utils;

import com.forest.utils.UnitTest;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HttpUtilUnitTest extends UnitTest {

    @Test
    void testGETRequestSuccess() throws Exception {
        String uri = "https://api.themoviedb.org/3/search/movie?api_key=3016d4342a491fa8cf0f37cb8bf5413c&query=Dont%27%20look%20up";
        HttpResponse<String> response = HttpUtil.get(uri);
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    void testGETRequestFail() throws Exception {
        String uri = "https://api.themoviedb.org/3/search/movie?query=Dont%27%20look%20up";
        HttpResponse<String> response = HttpUtil.get(uri);
        assertThat(response.statusCode()).isNotEqualTo(200);
    }
}