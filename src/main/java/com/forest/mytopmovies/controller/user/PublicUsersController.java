package com.forest.mytopmovies.controller.user;

import com.forest.mytopmovies.entity.User;
import com.forest.mytopmovies.params.UserParam;
import com.forest.mytopmovies.service.user.UserAuthService;
import com.forest.mytopmovies.service.user.UserCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/users")
public class PublicUsersController {
    @Autowired
    private UserAuthService authService;

    @Autowired
    private UserCrudService userCrudService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(@RequestBody UserParam user) {
        userCrudService.save(
                User.builder()
                        .withUsername(user.username())
                        .withPassword(passwordEncoder.encode(user.password()))
                        .withEmail(user.email())
                        .withActive(true)
                        .build()
        );
        return login(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserParam user) {
        return authService.login(user.username(), user.password()).orElseThrow(() -> new RuntimeException("Invalid login and/or password"));
    }
}
