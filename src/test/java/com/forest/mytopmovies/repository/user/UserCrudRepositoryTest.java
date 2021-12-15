package com.forest.mytopmovies.repository.user;

import com.forest.mytopmovies.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserCrudRepositoryTest {

    @Autowired
    private UserCrudRepository underTest;

    @Test
    void canFindActiveUserByUsername() {
        setUpActiveUser();
        Optional<User> user = underTest.findByUsername("forest1");
        assertThat(user).isPresent();
    }

    @Test
    void cannotFindInactiveUserByUsername() {
        setUpInactiveUser();
        Optional<User> user = underTest.findByUsername("forest2");
        assertThat(user).isNotPresent();
    }

    private void setUpActiveUser() {
        User user = User.builder().withId("test").withUsername("forest1").withPassword("123").withIsActive(true).build();
        underTest.saveAndFlush(user);
    }

    private void setUpInactiveUser() {
        User user = User.builder().withId("test").withUsername("forest2").withPassword("123").withIsActive(false).build();
        underTest.saveAndFlush(user);
    }
}