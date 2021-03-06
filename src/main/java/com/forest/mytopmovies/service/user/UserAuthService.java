package com.forest.mytopmovies.service.user;

import com.forest.mytopmovies.datamodels.entity.User;
import java.util.Optional;

public interface UserAuthService {
    Optional<String> login(String username, String password);

    Optional<User> findByToken(String token);

}
