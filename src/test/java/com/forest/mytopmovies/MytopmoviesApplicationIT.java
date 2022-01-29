package com.forest.mytopmovies;

import com.forest.mytopmovies.controller.user.UsersController;
import com.forest.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MytopmoviesApplicationIT extends IntegrationTest {

    @Autowired
    private UsersController usersController;

    @Test
    void contextLoads() {
        assertThat(usersController).isNotNull();
    }
}