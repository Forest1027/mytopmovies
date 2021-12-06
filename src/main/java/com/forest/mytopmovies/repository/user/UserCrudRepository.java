package com.forest.mytopmovies.repository.user;

import com.forest.mytopmovies.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserCrudRepository extends JpaRepository<User, String> {
    @Query("SELECT u from User u WHERE u.username = ?1")
    Optional<User> findByUsername(String username);
}
