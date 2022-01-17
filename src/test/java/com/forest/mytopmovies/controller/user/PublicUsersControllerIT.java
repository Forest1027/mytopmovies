package com.forest.mytopmovies.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forest.mytopmovies.entity.User;
import com.forest.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class PublicUsersControllerIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void canRegister() throws Exception {
        // given
        User user = defaultUser();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);

        // when
        MvcResult result = this.mockMvc.perform(post("/public/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        // then
        assertThat(result.getResponse().getContentAsString()).containsPattern(".+\\..+\\..+");
    }

    private static User defaultUser() {
        return User.builder()
                .username("forest")
                .password("123456").build();
    }
}