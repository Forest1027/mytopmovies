package com.forest.mytopmovies.controller.user;

import com.forest.mytopmovies.datamodels.entity.User;
import com.forest.mytopmovies.datamodels.params.user.UserCreateParam;
import com.forest.mytopmovies.datamodels.params.user.UserLoginParam;
import com.forest.mytopmovies.service.user.UserAuthService;
import com.forest.mytopmovies.service.user.UserCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity<String> register(@Valid @RequestBody UserCreateParam user) {
        userCrudService.save(
                User.builder()
                        .username(user.username())
                        .password(passwordEncoder.encode(user.password()))
                        .email(user.email())
                        .active(true)
                        .build()
        );
        return login(new UserLoginParam(user.username(), user.password()));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginParam user) {
        return new ResponseEntity<>(authService.login(user.username(), user.password()).orElseThrow(() -> new RuntimeException("Invalid login and/or password")), HttpStatus.OK);
    }
}
