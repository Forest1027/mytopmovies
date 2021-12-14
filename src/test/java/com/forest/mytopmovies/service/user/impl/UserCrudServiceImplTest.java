package com.forest.mytopmovies.service.user.impl;

import com.forest.mytopmovies.entity.User;
import com.forest.mytopmovies.repository.user.UserCrudRepository;
import com.forest.mytopmovies.service.user.UserCrudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserCrudServiceImplTest {
    private UserCrudService underTest;

    @Mock
    private UserCrudRepository userCrudRepository;

    @BeforeEach
    void setUp() {
        underTest = new UserCrudServiceImpl(userCrudRepository);
    }

    @Test
    void canSave() {
        // given
        User user = User.builder().withUsername("forest").withPassword("123").build();
        // when
        underTest.save(user);
        // then
        verify(userCrudRepository).save(user);
    }

    @Test
    void canFind() {
        // given
        String id = "test";
        // when
        underTest.find(id);
        // then
        verify(userCrudRepository).findById(id);
    }

    @Test
    void canFindByUsername() {
        // given
        String username = "forest";
        // when
        underTest.findByUsername(username);
        // then
        verify(userCrudRepository).findByUsername(username);
    }
}