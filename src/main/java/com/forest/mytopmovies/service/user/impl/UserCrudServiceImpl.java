package com.forest.mytopmovies.service.user.impl;

import com.forest.mytopmovies.datamodels.entity.User;
import com.forest.mytopmovies.exceptions.UserExistsException;
import com.forest.mytopmovies.repository.user.UserCrudRepository;
import com.forest.mytopmovies.service.user.UserCrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserCrudServiceImpl implements UserCrudService {
    private final UserCrudRepository userCrudRepository;

    public UserCrudServiceImpl(UserCrudRepository userCrudRepository) {
        this.userCrudRepository = userCrudRepository;
    }

    @Override
    public User save(User user) {
        if (findOneByUsername(user.getUsername()).isPresent())
            throw new UserExistsException(user.getUsername());
        return userCrudRepository.save(user);
    }

    @Override
    public Optional<User> findOneByUsername(String username) {
        return userCrudRepository.findOneByUsernameAndActive(username, true);
    }

}
