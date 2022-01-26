package com.forest.mytopmovies.repository.movie;

import com.forest.mytopmovies.datamodels.entity.MovieList;
import com.forest.mytopmovies.datamodels.entity.User;
import com.forest.mytopmovies.repository.user.UserCrudRepository;
import com.forest.utils.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MovieListRepositoryIT extends IntegrationTest {

    @Autowired
    private MovieListRepository underTest;

    @Autowired
    private UserCrudRepository userCrudRepository;

    private static String userId;

    @BeforeEach
    void setup() {
        String username = "forest";
        User user = User.builder().username(username).password("123456").active(true).build();
        Optional<User> userRes = userCrudRepository.findOneByUsernameAndActive(username, true);
        if (!userRes.isPresent()) {
            user = userCrudRepository.saveAndFlush(user);
            userId = user.getId();
            underTest.saveAndFlush(MovieList.builder().movieListName("test1").user(user).build());
            underTest.saveAndFlush(MovieList.builder().movieListName("test2").user(user).build());
        }
    }

    @Test
    void canFindAllByUserId() {
        // given

        // when
        Page<MovieList> result = underTest.findAllByUserId(userId, Pageable.ofSize(5).withPage(0));

        // then
        assertThat(result.getNumberOfElements()).isEqualTo(2);
    }

    @Test
    void canFindAllByUserIdAndMovieListName() {
        // given

        // when
        Page<MovieList> result = underTest.findAllByUserIdAndMovieListName(userId, "test1", Pageable.ofSize(5).withPage(0));

        // then
        assertThat(result.getNumberOfElements()).isEqualTo(1);
    }

}