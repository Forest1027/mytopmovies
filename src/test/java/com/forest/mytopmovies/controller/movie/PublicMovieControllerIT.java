package com.forest.mytopmovies.controller.movie;

import com.forest.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class PublicMovieControllerIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void canSearchMovieByNameWithDefaultPage() throws Exception {
        // given
        String movieName = "Don't look up";
        // when

        // then
        MvcResult mvcResult = this.mockMvc.perform(get("/public/movies?movieName=" + movieName))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
}