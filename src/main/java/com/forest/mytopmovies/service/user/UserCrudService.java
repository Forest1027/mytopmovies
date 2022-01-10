package com.forest.mytopmovies.service.user;

import com.forest.mytopmovies.entity.User;
import java.util.Optional;

/**
 * User service for CRUD operations
 */
public interface UserCrudService {
    User save(User user);

    Optional<User> findOneById(String id);

    Optional<User> findOneByUsername(String username);
}
