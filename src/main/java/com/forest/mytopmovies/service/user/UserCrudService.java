package com.forest.mytopmovies.service.user;

import com.forest.mytopmovies.entity.User;
import java.util.Optional;

/**
 * User service for CRUD operations
 */
public interface UserCrudService {
    User save(User user);

    Optional<User> find(String id);

    Optional<User> findByUsername(String username);
}
