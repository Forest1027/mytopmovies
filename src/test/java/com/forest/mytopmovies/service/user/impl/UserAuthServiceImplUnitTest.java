package com.forest.mytopmovies.service.user.impl;

import com.forest.mytopmovies.service.user.TokenService;
import com.forest.mytopmovies.service.user.UserAuthService;
import com.forest.mytopmovies.service.user.UserCrudService;
import com.forest.utils.UnitTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class UserAuthServiceImplUnitTest extends UnitTest {
    private UserAuthService underTest;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserCrudService userCrudService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        underTest = new UserAuthServiceImpl(tokenService, userCrudService, passwordEncoder);
    }

    @AfterEach
    void teardown() {
        verifyNoMoreInteractions(tokenService, userCrudService, passwordEncoder);
    }

    @Test
    void canInteractWithUserCrudService() {
        // given
        String username = "forest";
        String password = "123";
        // when
        underTest.login(username, password);
        // then
        verify(userCrudService).findOneByUsername(username);
    }

    @Test
    void canInteractWithTokenService() {
        // given
        String token = "test-token";
        // when
        underTest.findByToken(token);
        // then
        verify(tokenService).verify(token);
    }

}