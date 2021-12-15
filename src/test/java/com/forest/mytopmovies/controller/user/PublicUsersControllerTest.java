package com.forest.mytopmovies.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forest.mytopmovies.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PublicUsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void canRegister() throws Exception {
        User user = defaultUser();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);

        this.mockMvc.perform(post("/public/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void canLogin() throws Exception {
        /*User user = defaultUser();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/public/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isUnauthorized());*/

    }

    private static User defaultUser() {
        return User.builder()
                .withUsername("forest")
                .withPassword("123456").build();
    }
}