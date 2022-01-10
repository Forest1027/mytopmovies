package com.forest.mytopmovies.repository.user;

import com.forest.mytopmovies.entity.User;
import com.forest.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
        Optional<User> user = underTest.findOneByUsernameAndActiveIsTrue("forest1");
        // then
        assertThat(user).isPresent();
    }

    @Test
    void cannotFindInactiveUserByUsername() {
        // given
        setUpInactiveUser();
        // when
        Optional<User> user = underTest.findOneByUsernameAndActiveIsTrue("forest2");
        // then
        assertThat(user).isNotPresent();
    }

    private void setUpActiveUser() {
        User user = User.builder().withId("test").withUsername("forest1").withPassword("123").withActive(true).build();
        underTest.saveAndFlush(user);
    }

    private void setUpInactiveUser() {
        User user = User.builder().withId("test").withUsername("forest2").withPassword("123").withActive(false).build();
        underTest.saveAndFlush(user);
    }
}