package com.forest.mytopmovies.service.user.impl;

import com.forest.mytopmovies.entity.User;
import com.forest.mytopmovies.service.user.TokenService;
import com.forest.mytopmovies.service.user.UserAuthService;
import com.forest.mytopmovies.service.user.UserCrudService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserAuthServiceImpl implements UserAuthService {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserCrudService userCrudService;

    @Override
    public Optional<String> login(String username, String password) {
        return userCrudService.findByUsername(username)
                .filter(user -> Objects.equals(password, user.getPassword()))
                .map(user -> tokenService.expiring(ImmutableMap.of("username", username)));
    }

    @Override
    public Optional<User> findByToken(String token) {
        return Optional.of(tokenService.verify(token))
                .map(map -> map.get("username"))
                .flatMap(userCrudService::findByUsername);
    }

    @Override
    public void logout(User user) {

    }
}
