package com.forest.mytopmovies.service.user.impl;

import com.forest.mytopmovies.entity.User;
import com.forest.mytopmovies.service.user.TokenService;
import com.forest.mytopmovies.service.user.UserAuthService;
import com.forest.mytopmovies.service.user.UserCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserAuthServiceImpl implements UserAuthService {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserCrudService userCrudService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<String> login(String username, String password) {
        return userCrudService.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> tokenService.expiring(Map.of("username", username)));
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

    public UserAuthServiceImpl(TokenService tokenService, UserCrudService userCrudService, PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.userCrudService = userCrudService;
        this.passwordEncoder = passwordEncoder;
    }
}
