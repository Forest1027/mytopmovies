package com.forest.mytopmovies;

import com.forest.mytopmovies.controller.user.PublicUsersController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class MytopmoviesApplicationTest {

    @Autowired
    private PublicUsersController publicUsersController;

    @Test
    void contextLoads() {
        assertThat(publicUsersController).isNotNull();
    }
}