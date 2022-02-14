package com.forest.mytopmovies.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forest.mytopmovies.datamodels.entity.User;
import com.forest.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class UserControllerIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void canRegister() throws Exception {
        // given
        User user = defaultUser("forest1");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);

        // when
        MvcResult result = this.mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        // then
        assertThat(result.getResponse().getContentAsString()).containsPattern(".+\\..+\\..+");
    }

    @Test
    void cantRegister() throws Exception {
        // given
        User user = defaultUser("forest2");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        // when

        // then
        this.mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Please use a different username to register.")))
                .andReturn();
    }
    
    @Test
    void cantLogin() throws Exception {
        // given
        User user = defaultUser("forest3");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);

        // when
        MvcResult result = this.mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isUnauthorized()).andReturn();

        // then
        assertThat(result.getResponse().getContentAsString()).contains("Invalid login and/or password");
    }

    private static User defaultUser(String username) {
        return User.builder()
                .username(username)
                .password("123456").build();
    }
}