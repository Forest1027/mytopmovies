package com.forest.mytopmovies.repository.user;

import com.forest.mytopmovies.datamodels.entity.User;
import com.forest.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserCrudRepositoryIT extends IntegrationTest {

    @Autowired
    private UserCrudRepository underTest;

    @Test
    void canFindActiveUserByUsername() {
        // given
        setUpActiveUser();
        // when
        Optional<User> user = underTest.findOneByUsernameAndActive("forest1", true);
        // then
        assertThat(user).isPresent();
    }

    @Test
    void cannotFindInactiveUserByUsername() {
        // given
        setUpInactiveUser();
        // when
        Optional<User> user = underTest.findOneByUsernameAndActive("forest2", true);
        // then
        assertThat(user).isNotPresent();
    }

    private void setUpActiveUser() {
        User user = User.builder().id("test").username("forest1").password("123").active(true).build();
        underTest.saveAndFlush(user);
    }

    private void setUpInactiveUser() {
        User user = User.builder().id("test").username("forest2").password("123").active(false).build();
        underTest.saveAndFlush(user);
    }
}