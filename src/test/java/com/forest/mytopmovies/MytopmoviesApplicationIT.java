package com.forest.mytopmovies;

import com.forest.mytopmovies.controller.user.UserController;
import com.forest.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MytopmoviesApplicationIT extends IntegrationTest {

    @Autowired
    private UserController userController;

    @Test
    void contextLoads() {
        assertThat(userController).isNotNull();
    }
}