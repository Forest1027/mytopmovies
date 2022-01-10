package com.forest.mytopmovies;

import com.forest.mytopmovies.controller.user.PublicUsersController;
import com.forest.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MytopmoviesApplicationIT extends IntegrationTest {

    @Autowired
    private PublicUsersController publicUsersController;

    @Test
    void contextLoads() {
        assertThat(publicUsersController).isNotNull();
    }
}