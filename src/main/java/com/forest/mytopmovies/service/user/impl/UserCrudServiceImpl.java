package com.forest.mytopmovies.service.user.impl;

import com.forest.mytopmovies.entity.User;
import com.forest.mytopmovies.repository.user.UserCrudRepository;
import com.forest.mytopmovies.service.user.UserCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserCrudServiceImpl implements UserCrudService {
    @Autowired
    private UserCrudRepository userCrudRepository;

    @Override
    public User save(User user) {
        return userCrudRepository.save(user);
    }

    @Override
    public Optional<User> find(String id) {
        return userCrudRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userCrudRepository.findByUsername(username);
    }
}
