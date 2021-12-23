package com.forest.mytopmovies.controller.user;

import com.forest.mytopmovies.entity.User;
import com.forest.mytopmovies.params.UserCreateParam;
import com.forest.mytopmovies.params.UserLoginParam;
import com.forest.mytopmovies.service.user.UserAuthService;
import com.forest.mytopmovies.service.user.UserCrudService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/users")
public class PublicUsersController {
    private final UserAuthService authService;

    private final UserCrudService userCrudService;

    private final PasswordEncoder passwordEncoder;

    public PublicUsersController(UserAuthService authService, UserCrudService userCrudService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.userCrudService = userCrudService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String register(@RequestBody UserCreateParam user) {
        userCrudService.save(
                User.builder()
                        .withUsername(user.username())
                        .withPassword(passwordEncoder.encode(user.password()))
                        .withEmail(user.email())
                        .withActive(true)
                        .build()
        );
        return login(new UserLoginParam(user.username(), user.password()));
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginParam user) {
        return authService.login(user.username(), user.password()).orElseThrow(() -> new RuntimeException("Invalid login and/or password"));
    }
}
