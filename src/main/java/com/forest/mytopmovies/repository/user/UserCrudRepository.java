package com.forest.mytopmovies.repository.user;

import com.forest.mytopmovies.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCrudRepository extends JpaRepository<User, String> {
    Optional<User> findOneByUsernameAndActive(String username, boolean active);
}
