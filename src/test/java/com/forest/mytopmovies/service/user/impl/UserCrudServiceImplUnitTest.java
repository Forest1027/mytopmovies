package com.forest.mytopmovies.service.user.impl;

import com.forest.mytopmovies.datamodels.entity.User;
import com.forest.mytopmovies.exceptions.UserExistsException;
import com.forest.mytopmovies.repository.user.UserCrudRepository;
import com.forest.mytopmovies.service.user.UserCrudService;
import com.forest.utils.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserCrudServiceImplUnitTest extends UnitTest {
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
        User user = User.builder().username("forest").password("123").build();

        // when
        underTest.save(user);

        // then
        verify(userCrudRepository).save(user);
    }

    @Test
    void willThrowUserExistException() {
        // given
        String username = "forest";
        User user = User.builder().username(username).password("123").build();
        when(userCrudRepository.findOneByUsernameAndActive(username, true)).thenReturn(Optional.of(user));
        String expectedException = "User with username " + username + " already exists. Please use a different username to register.";

        // when
        UserExistsException exception = assertThrows(UserExistsException.class, () -> {
            underTest.save(user);
        });

        // then
        assertEquals(exception.getMessage(), expectedException);
    }

    @Test
    void canFind() {
        // given
        String id = "test";

        // when
        underTest.findOneById(id);

        // then
        verify(userCrudRepository).findById(id);
    }

    @Test
    void canFindByUsername() {
        // given
        String username = "forest";

        // when
        underTest.findOneByUsername(username);

        // then
        verify(userCrudRepository).findOneByUsernameAndActive(username, true);
    }
}